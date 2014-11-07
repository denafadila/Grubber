package skripsi.com.grubber;

import java.util.List;

import skripsi.com.android.Utility;
import skripsi.com.android.widget.Validate;
import skripsi.com.grubber.dao.UserDao;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class RegisterFragment extends Fragment {
  private static final int PHOTO_REQUEST_CODE = 678;
  private static final String TAG = RegisterFragment.class.getSimpleName();

  private OnFragmentInteractionListener mListener;

  private ImageButton mPhoto = null;
  private byte[] photoBitmap = null;
  private Bitmap photoThumbnail = null;
  private EditText mEmail = null;
  private TextView tvPass = null;
  private TextView tvPass2 = null;
  private EditText mPassword = null;
  private EditText mPassword2 = null;
  private EditText mUsername = null;
  private EditText mFullName = null;
  private EditText mAboutMe = null;
  private CheckBox mAgree = null;
  private TextView mTnC = null;
  private Bundle bundle;
  private boolean mIsSubmitting = false;

  protected boolean validateForm() {
    boolean result = false;

    result = validateEmail(true) && validatePassword() && validatePassword2() && validateUsername()
        && validateFullName() && validateAboutMe();
    if (result) {
      // check Photo
    }
    return result;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    setHasOptionsMenu(true);
    if (ParseUser.getCurrentUser() != null) {
      getActivity().supportInvalidateOptionsMenu();
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_register, container, false);

    mPhoto = (ImageButton) view.findViewById(R.id.ibRegistrationPhoto);
    mPhoto.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        setPhoto();
      }
    });
    mPhoto.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        mPhoto.setImageResource(R.drawable.ic_launcher_new);
        if (photoBitmap != null) {
          photoBitmap = null;
        }
        if (photoThumbnail != null) {
          photoThumbnail.recycle();
          photoThumbnail = null;
        }
        return true;
      }
    });

    mEmail = (EditText) view.findViewById(R.id.etRegistrationEmail);
    mEmail.setFilters(new InputFilter[] { new LengthFilter(100) });
    mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
          validateEmail(false);
        }
      }
    });
    tvPass = (TextView) view.findViewById(R.id.tvRegistrationPassword);
    tvPass2 = (TextView) view.findViewById(R.id.tvRegistrationPassword2);
    mPassword = (EditText) view.findViewById(R.id.etRegistrationPassword);
    mPassword.setFilters(new InputFilter[] { new LengthFilter(33) });
    mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
          validatePassword();
        }
      }
    });

    mPassword2 = (EditText) view.findViewById(R.id.etRegistrationPassword2);
    mPassword2.setFilters(new InputFilter[] { new LengthFilter(33) });
    mPassword2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
          validatePassword2();
        }
      }
    });

    mUsername = (EditText) view.findViewById(R.id.etRegistrationUserName);
    mUsername.setFilters(new InputFilter[] { new LengthFilter(33) });
    mUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
          validateUsername();
        }
      }
    });

    mFullName = (EditText) view.findViewById(R.id.etRegistrationFullName);
    mFullName.setFilters(new InputFilter[] { new LengthFilter(33) });
    mFullName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
          validateFullName();
        }
      }
    });

    mAboutMe = (EditText) view.findViewById(R.id.etRegistrationAboutMe);
    mAboutMe.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
          validateAboutMe();
        }
      }
    });

    updateView();
    return view;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.registration, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public void onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    menu.findItem(R.id.miRegistrationOk).setEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.miRegistrationOk:
      submitRegistration();
      break;
    case R.id.miRegistrationCancel:
      getActivity().onBackPressed();
      break;
    }
    return super.onOptionsItemSelected(item);
  }

  private void updateView() {
    // control visibility
    // in registration mode
    mEmail.setEnabled(true);
    mUsername.setEnabled(true);
    mFullName.setEnabled(true);
    // normal editable mode
    mPhoto.setEnabled(true);
    mAboutMe.setEnabled(true);

  }

  protected void setPhoto() {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    startActivityForResult(intent, PHOTO_REQUEST_CODE);
  }

  protected void submitRegistration() {
    if (photoBitmap == null) {
      AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
      alert.setMessage(R.string.grubber_registration_profile_picture_warning);
      alert.setPositiveButton(R.string.grubber_registration_profile_picture_warning_yes,
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              setPhoto();
            }
          });
      alert.setNegativeButton(R.string.grubber_registration_profile_picture_warning_no,
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              // user skips photo
              new RegisterTask().execute();
            }
          });
      alert.show();
    } else {
      new RegisterTask().execute();
    }
  }

  protected void showToS() {
    TNCDialogFragment tdf = TNCDialogFragment.newInstance(TNCDialogFragment.TYPE_TERMS_OF_SERVICE);
    tdf.show(getFragmentManager(), null);
  }

  protected void showPP() {
    TNCDialogFragment tdf = TNCDialogFragment.newInstance(TNCDialogFragment.TYPE_PRIVACY_POLICY);
    tdf.show(getFragmentManager(), null);
  }

  protected boolean validateAboutMe() {
    boolean result = true;
    return result;
  }

  protected boolean validateEmail(boolean isSync) {
    boolean result = true;
    if (!Validate.hasText(mEmail)) {
      mEmail.setError(getResources().getText(R.string.grubber_registration_username_required));
      result = false;
    } else if (!Validate.isEmailAddress(mEmail, true)) {
      mEmail.setError(getResources().getText(R.string.grubber_registration_username_invalid));
      result = false;
    } else {
      // check if username is taken
      ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
      userQuery.whereEqualTo("email", mEmail.getText().toString().trim());
      if (!isSync) {
        userQuery.findInBackground(new FindCallback<ParseUser>() {
          @Override
          public void done(List<ParseUser> users, ParseException e) {
            if (e == null) {
              if (users != null && users.size() > 0) {
                mEmail.setError(getResources()
                    .getText(R.string.grubber_registration_username_taken));
              }
            } else {
              Log.w(TAG, "Problem checking existing username", e);
              Toast.makeText(getActivity(), getResources().getText(R.string.grubber_backend_error),
                  Toast.LENGTH_LONG).show();
            }
          }
        });
      } else {
        result = false;
        try {
          List<ParseUser> users = userQuery.find();
          if (users != null && users.size() > 0) {
            mEmail.setError(getResources().getText(R.string.grubber_registration_username_taken));
          } else {
            result = true;
          }
        } catch (ParseException e) {
          Log.w(TAG, "Problem checking existing username", e);
          Toast.makeText(getActivity(), getResources().getText(R.string.grubber_backend_error),
              Toast.LENGTH_LONG).show();
        }
      }
    }
    return result;
  }

  protected boolean validatePassword() {
    boolean result = true;
    if (!Validate.hasText(mPassword) || !Validate.isValid(mPassword, Validate.regex_password, true)) {
      mPassword.setError(getResources().getText(R.string.grubber_registration_password_required));
      result = false;
    }
    return result;
  }

  protected boolean validatePassword2() {
    boolean result = true;
    if (mPassword.getError() == null
        && !mPassword.getText().toString().equals(mPassword2.getText().toString())) {
      mPassword2.setError(getResources().getText(R.string.grubber_registration_password2_mismatch));
      result = false;
    }
    return result;
  }

  protected boolean validateUsername() {
    boolean result = true;

    if (!Validate.hasText(mUsername) || !Validate.hasTextLength(mUsername.getText(), 3, 33)) {
      mUsername.setError(getResources().getText(R.string.grubber_registration_screenname_required));
      result = false;
    } else if (!Validate.isValid(mUsername, "^[^\\t\\n\\x0B\\f\\r]{3,33}$", true)) {
      mUsername.setError(getResources().getText(R.string.grubber_registration_screenname_invalid));
      result = false;
    }
    return result;
  }

  protected boolean validateFullName() {
    boolean result = true;
    if (!Validate.hasText(mFullName) || !Validate.hasTextLength(mFullName.getText(), 3, 33)) {
      mFullName.setError(getResources().getText(R.string.grubber_registration_full_name_required));
      result = false;
    } else if (!Validate.isValid(mFullName.getText(), "^[^\\t\\n\\x0B\\f\\r]{3,33}$", true)) {
      mFullName.setError(getResources().getText(R.string.grubber_registration_full_name_invalid));
      result = false;
    }
    return result;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (OnFragmentInteractionListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  public interface OnFragmentInteractionListener {
    public void onSuccessfulRegistration();

  }

  class RegisterTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      Utility.lockScreenOrientation(getActivity());
      // show progress bar
    }

    @Override
    protected Void doInBackground(Void... params) {
      try {
        UserDao.registerUser(getActivity(), mEmail.getText().toString().trim(), mPassword.getText()
            .toString().trim(), Utility.getTrimmedText(mUsername.getText()),
            Utility.getTrimmedText(mFullName.getText()),
            Utility.getTrimmedText(mAboutMe.getText()), photoBitmap, photoThumbnail);
      } catch (ParseException e) {
        Log.w(TAG, "Problem while signing up user", e);
        getView().post(new Runnable() {
          @Override
          public void run() {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage(getActivity().getString(R.string.grubber_backend_error));
            alert.show();
          }
        });
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void result) {
      super.onPostExecute(result);
      Utility.unlockScreenOrientation(getActivity());
      mListener.onSuccessfulRegistration();
    }
  }

}
