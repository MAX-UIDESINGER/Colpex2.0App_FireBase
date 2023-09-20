package com.example.colpex20app_firebase.LoginRegistro;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.airbnb.lottie.LottieAnimationView;
import com.example.colpex20app_firebase.OperadorResponsable.PanelOperador;
import com.example.colpex20app_firebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class FragmentRegistro extends Fragment {
    //imagen
    ImageView userPhoto;
    FloatingActionButton fab;
    Uri pickedImgUri;
    private File f;
    static int PreReqCode = 1;
    static int REQUESCODE = 1;
    //nueva camara
    private static final int ACCESS_FILE = 23;
    private static final int PERMISSION_FILE = 43;
    //loginAmimacion progreso bar
    LottieAnimationView lottieAnimationView;
    Button Registrardatos;
    TextInputEditText edtUsuario, edtApellidoPaternoU, edtApellidoMaternoU, edtCorreo, editphone, edtPassword, edtPasswordconf;
    //TexImpuLayout
    TextInputLayout txtInputUsuario, txtInputApellidoPaternoU, txtInputApellidoMaternoU, txtInputArea, txtInputCorreo, txtInputPassword, txtInputPasswordconf;
    //Firebase
    private String TAG;
    String userID;
    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    //numero de registro  countryCodePicker
    CountryCodePicker countryCodePicker;
    //Auto Complete
    AutoCompleteTextView txAutoCompleteAreas;
    ArrayList<String> allNamesAreas;
    //
    Button signInText;
    //TooLL bar
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Tool Bar Metodo
        ToolbarMetodos(view);
        //Firebase
        InicarFirebase();
        //CAMPOS
        CamposDefinidos(view);
        //Enviar al Login
        EnviaralLogin();
        // Imagen insertar
        ButtonImagenButtomFloatin(view);
        //combo Areas
        ArrayAdacterAreas();
        //button de Iniciar Registro
        RegistrardatosOperador(view);
    }

    private void ToolbarMetodos(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        // getActivity().setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.nav_presentacion);


            }
        });
    }

    private void EnviaralLogin() {
        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_login);
            }
        });
    }

    private void CamposDefinidos(View view) {
        edtUsuario = view.findViewById(R.id.edtUsuario);
        edtApellidoPaternoU = view.findViewById(R.id.edtApellidoPaternoU);
        edtApellidoMaternoU = view.findViewById(R.id.edtApellidoMaternoU);
        edtCorreo = view.findViewById(R.id.edtCorreo);
        //numero de telefono
        editphone = view.findViewById(R.id.signup_phone_number);
        countryCodePicker = view.findViewById(R.id.contry_code_picker);
        //
        edtPassword = view.findViewById(R.id.edtPassword);
        edtPasswordconf = view.findViewById(R.id.edtPasswordconf);
        txAutoCompleteAreas = view.findViewById(R.id.dropdownArea);
        //
        signInText = view.findViewById(R.id.btnRegistate);
        //Progrersor Bar
        lottieAnimationView = view.findViewById(R.id.lottie);
        lottieAnimationView.setVisibility(View.INVISIBLE);
        //TxxImputUsuarios
        txtInputUsuario = view.findViewById(R.id.txtInputUsuario);
        txtInputApellidoPaternoU = view.findViewById(R.id.txtInputApellidoPaternoU);
        txtInputApellidoMaternoU = view.findViewById(R.id.txtInputApellidoMaternoU);
        txtInputArea = view.findViewById(R.id.txtInputArea);
        txtInputCorreo = view.findViewById(R.id.txtInputCorreo);
        txtInputPassword = view.findViewById(R.id.txtInputPassword);
        txtInputPasswordconf = view.findViewById(R.id.txtInputPasswordconf);
    }

    private void RegistrardatosOperador(View view) {

        Registrardatos = view.findViewById(R.id.Registrardatos);
        Registrardatos.setOnClickListener(v -> {
            //conecion de Internet
            if (!isConnected()) {
                showCustomDialog();
            } else {
                Registrardatos.setVisibility(View.INVISIBLE);
                lottieAnimationView.setVisibility(View.VISIBLE);
                final String email = edtCorreo.getText().toString();
                //final String phone = editphone.getText().toString();
                // final String _phoneNo = "+" + countryCodePicker.getFullNumber()+phone;
                final String password = edtPassword.getText().toString();
                final String password2 = edtPasswordconf.getText().toString();
                final String name = edtUsuario.getText().toString();
                final String surnamePa = edtApellidoPaternoU.getText().toString();
                final String surnameMa = edtApellidoMaternoU.getText().toString();
                final String area = txAutoCompleteAreas.getText().toString();
                final String imagUrl = userPhoto.getDrawable().toString();
                if (imagUrl.isEmpty() || name.isEmpty() || surnamePa.isEmpty() ||
                        surnameMa.isEmpty() || area.isEmpty() || email.isEmpty() ||
                        password.isEmpty() || !password.equals(password2)) {
                    validar();
                    ShowSnackbar("Verifique todos los campos");
                    Registrardatos.setVisibility(View.VISIBLE);
                    lottieAnimationView.setVisibility(View.INVISIBLE);
                } else {
                    CrearUserContendio(email, name, surnamePa, surnameMa, password, area);
                    LimpiarText();
                }
                EditorTexto();
            }
        });
    }

    private void CrearUserContendio(String email, String name, String surnamePa, String surnameMa, String password, String area) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            showMessage("Cuenta creada");
                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("UserColpex").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("Name", name);
                            user.put("surnamePa", surnamePa);
                            user.put("surnameMa", surnameMa);
                            user.put("Email", email);
                            user.put("Password", password);
                            user.put("isUser", area);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });

                            if (pickedImgUri != null) {
                                updateUserInfo(name, pickedImgUri, mAuth.getCurrentUser());
                            } else {
                                updateUserInfoWithoutPhoto(name, mAuth.getCurrentUser());
                            }

                        } else {
                            // account creation failed
                            ShowSnackbar("la creación de la cuenta falló" + task.getException().getMessage());
                            Registrardatos.setVisibility(View.VISIBLE);
                            lottieAnimationView.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void InicarFirebase() {
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    private void updateUserInfo(String name, Uri pickedImgUri, FirebaseUser currentUser) {
        // primero necesitamos subir la foto del usuario al almacenamiento de firebase y obtener la url
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("FotosUser");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // imagen cargada con exito ahora podemos obtener la url de nuestra imagen
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // uri contiene la URL de la imagen del usuario
                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // información de usuario actualizada con éxito
                                            showMessage("Register Complete");
                                            updateUI();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    private void updateUserInfoWithoutPhoto(String name, FirebaseUser currentUser) {
        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        currentUser.updateProfile(profleUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // información de usuario actualizada con éxito
                            showMessage("Register Complete");
                            updateUI();
                        }
                    }
                });
    }

    private void updateUI() {
        Intent homeActivity = new Intent(getContext(), PanelOperador.class);
        startActivity(homeActivity);
        getActivity().finish();
    }

    //Alerta para Internet
    private void showCustomDialog() {
        new SweetAlertDialog(getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Ups...")
                .setContentText("Por favor, conéctese a Internet para continuar")
                .setConfirmText("conectarse")
                .setCustomImage(com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_wifi_off_24_regular)
                .setConfirmButtonTextColor(Color.parseColor("#FFFFFF"))
                .setConfirmButtonBackgroundColor(Color.parseColor("#000000"))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .show();

    }

    private void ShowSnackbar(String s) {
        Snackbar snackbar = Snackbar.make(Registrardatos, s, Snackbar.LENGTH_SHORT);
        snackbar.setTextColor(Color.parseColor("#ffffff"));
        snackbar.setBackgroundTint(Color.parseColor("#cc3c3c"));
        snackbar.show();
    }

    private void ButtonImagenButtomFloatin(View view) {
        fab = view.findViewById(R.id.FloImag);
        userPhoto = view.findViewById(R.id.imageUser);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FILE);
                    Toast.makeText(getContext(), "Por favor acepte los permisos requeridos ", Toast.LENGTH_SHORT).show();
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, REQUESCODE);
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == REQUESCODE && data != null) {
            pickedImgUri = data.getData();
            CropImage.activity(pickedImgUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    //.setActivityTitle("Crop Image")
                    .setFixAspectRatio(true)
                    // .setCropMenuCropButtonTitle("Done")
                    .start(getActivity(), this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                userPhoto.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void ArrayAdacterAreas() {
        allNamesAreas = Areas();
        ArrayAdapter<String> autoCompleteLotNumber = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, allNamesAreas);
        txAutoCompleteAreas.setAdapter(autoCompleteLotNumber);

    }

    private ArrayList<String> Areas() {
        ArrayList<String> allNameAreas = new ArrayList<>();
        allNameAreas.add("Operador responsable");
        return allNameAreas;
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void LimpiarText() {
        edtUsuario.setText("");
        edtApellidoPaternoU.setText("");
        edtApellidoMaternoU.setText("");
        edtCorreo.setText("");
        edtPassword.setText("");
        edtPasswordconf.setText("");
        txAutoCompleteAreas.setText("");
    }

    private boolean validar() {
        boolean retorno = true;
        String Usuario, ApellidoPaternoU, ApellidoMaternoU, Area, Correo,
                Password, Passwordconf;

        Usuario = edtUsuario.getText().toString();
        ApellidoPaternoU = edtApellidoPaternoU.getText().toString();
        ApellidoMaternoU = edtApellidoMaternoU.getText().toString();
        Area = txAutoCompleteAreas.getText().toString();
        Correo = edtCorreo.getText().toString();
        Password = edtPassword.getText().toString();
        Passwordconf = edtPasswordconf.getText().toString();

        if (this.pickedImgUri == null) {
            Toast.makeText(getContext(), "Debe selecionar una foto de perfil", Toast.LENGTH_SHORT).show();
            retorno = false;
        }
        if (Usuario.isEmpty()) {
            txtInputUsuario.setError("Ingrese su nombre");
            retorno = false;
        } else {
            txtInputUsuario.setErrorEnabled(false);
        }
        if (ApellidoPaternoU.isEmpty()) {
            txtInputApellidoPaternoU.setError("Ingrese su apellido Paterno");
            retorno = false;
        } else {
            txtInputApellidoPaternoU.setErrorEnabled(false);
        }
        if (ApellidoMaternoU.isEmpty()) {
            txtInputApellidoMaternoU.setError("Ingrese su apellido Materno");
            retorno = false;
        } else {
            txtInputApellidoMaternoU.setErrorEnabled(false);
        }

        if (Area.isEmpty()) {
            txtInputArea.setError("Ingrese su Area Trabajo");
            retorno = false;
        } else {
            txtInputArea.setErrorEnabled(false);


        }
        if (Correo.isEmpty()) {
            txtInputCorreo.setError("Ingrese su Correo");
            retorno = false;
        } else if (Correo.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(Correo).matches()) {
            txtInputCorreo.setError("correo invalido");
            retorno = false;
        } else {
            txtInputCorreo.setErrorEnabled(false);
        }

        if (Passwordconf.isEmpty()) {
            txtInputPassword.setError("Ingrese su contraseña");
            retorno = false;
        } else if (Password.isEmpty() || Password.length() < 8) {
            txtInputPassword.setError("Se necesitan mas de 8 caracteres");
            retorno = false;
        } else if (!Pattern.compile("[0-9]").matcher(Password).find()) {
            txtInputPassword.setError("Al menos un numero");
            retorno = false;
        } else if (!Passwordconf.equals(Password)) {
            txtInputPassword.setError("Deben ser Iguales");
            retorno = false;
        } else {
            txtInputPassword.setError(null);
        }

        if (Passwordconf.isEmpty()) {
            txtInputPasswordconf.setError("Ingrese su contraseña");
            retorno = false;
        } else if (Passwordconf.isEmpty() || Passwordconf.length() < 8) {
            txtInputPasswordconf.setError("Se necesitan mas de 8 caracteres");
            retorno = false;
        } else if (!Pattern.compile("[0-9]").matcher(Password).find()) {
            txtInputPasswordconf.setError("Al menos un numero");
            retorno = false;
        } else if (!Passwordconf.equals(Password)) {
            txtInputPasswordconf.setError("Deben ser Iguales");
            retorno = false;
        } else {
            txtInputPasswordconf.setError(null);
        }

        return retorno;
    }

    private void EditorTexto() {
        edtUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputUsuario.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtApellidoPaternoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputApellidoPaternoU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtApellidoMaternoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputApellidoMaternoU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txAutoCompleteAreas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputArea.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputCorreo.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtPasswordconf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputPasswordconf.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        @SuppressLint("MissingPermission") NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected() || (mobileConn != null && mobileConn.isConnected()))) {
            return true;
        } else {
            return false;

        }
    }

}