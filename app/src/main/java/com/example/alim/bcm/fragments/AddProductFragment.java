package com.example.alim.bcm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alim.bcm.R;
import com.example.alim.bcm.data.model.Articolo;
import com.example.alim.bcm.data.model.Attrezzo;
import com.example.alim.bcm.data.model.Materiale;
import com.example.alim.bcm.services.ConfermaDatiDialog;
import com.example.alim.bcm.utilities.FireBaseConnection;
import com.example.alim.bcm.utilities.InternalStorage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.alim.bcm.data.model.Constants.ADD_PRODUCT_FRAGMENT;
import static com.example.alim.bcm.data.model.Constants.ATTREZZI;
import static com.example.alim.bcm.data.model.Constants.CONFERMA_DATI_DIALOG;
import static com.example.alim.bcm.data.model.Constants.MATERIALI;
import static com.example.alim.bcm.data.model.Constants.TAG;


public class AddProductFragment extends Fragment {

    private EditText eNomeArticolo;
    private EditText eMarcaArticolo;
    private EditText eModelloArticolo;
    private EditText eTipoArticolo;
    private EditText eIdArticolo;
    private Button bAddProduct;
    private String tipoAritcolo = "";
    private Spinner spinnerAddProduct;
    DatabaseReference ref;
    FirebaseDatabase database;

    public AddProductFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        database = FirebaseDatabase.getInstance();
        ref = database.getReferenceFromUrl(FireBaseConnection.BASE_URL);
        return inflater.inflate(R.layout.fragment_add_product, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        eNomeArticolo = view.findViewById(R.id.eNomeArticolo);
        eMarcaArticolo = view.findViewById(R.id.eMarcaArticolo);
        eModelloArticolo = view.findViewById(R.id.eModelloArticolo);
        eTipoArticolo = view.findViewById(R.id.eTipoArticolo);
        eIdArticolo = view.findViewById(R.id.eIdArticolo);
        spinnerAddProduct = view.findViewById(R.id.spinnerAddProduct);
        bAddProduct = view.findViewById(R.id.bAddProduct);

        spinnerAddProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoAritcolo = spinnerAddProduct.getSelectedItem().toString();
                setIdArticolo(tipoAritcolo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setSpinner(spinnerAddProduct);

        bAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkCampo(eNomeArticolo) || !checkCampo(eMarcaArticolo) || !checkCampo(eModelloArticolo) || !checkCampo(eTipoArticolo) || !checkCampo(eIdArticolo)) {
                    Toast.makeText(getContext(), "CONTROLLARE I CAMPI", LENGTH_SHORT).show();
                } else {
                    ConfermaDatiDialog confermaDatiDialog = new ConfermaDatiDialog(ConfermaDatiDialog.GENERIC_MESSAGE, "stai per inserire un articolo", new ConfermaDatiDialog.onButtonClickListener() {
                        @Override
                        public void onCheckClick() {
                            if (tipoAritcolo.equalsIgnoreCase(ATTREZZI)) {
                                Attrezzo attrezzo = new Attrezzo();
                                attrezzo.setNome(eNomeArticolo.getText().toString());
                                attrezzo.setMarca(eMarcaArticolo.getText().toString());
                                attrezzo.setModello(eModelloArticolo.getText().toString());
                                attrezzo.setTipo(eTipoArticolo.getText().toString());
                                attrezzo.setId(eIdArticolo.getText().toString());
                                insertArticoloToDB(attrezzo);
                                InternalStorage.resetDB(getContext(), ATTREZZI);
                            } else if (tipoAritcolo.equalsIgnoreCase(MATERIALI)) {
                                Materiale materiale = new Materiale();
                                materiale.setNome(eNomeArticolo.getText().toString());
                                materiale.setMarca(eMarcaArticolo.getText().toString());
                                materiale.setModello(eModelloArticolo.getText().toString());
                                materiale.setTipo(eTipoArticolo.getText().toString());
                                materiale.setId(eIdArticolo.getText().toString());
                                insertArticoloToDB(materiale);
                                InternalStorage.resetDB(getContext(), MATERIALI);
                            } else {
                                Toast.makeText(getContext(), "CONTROLLARE I CAMPI", LENGTH_SHORT).show();
                                return;
                            }

                            RichiesteFragment richiesteFragment = new RichiesteFragment();
                            getFragmentManager().beginTransaction().replace(R.id.fragmentImpiegato, richiesteFragment, ADD_PRODUCT_FRAGMENT).commit();


                        }

                        @Override
                        public void onCloseClick() {

                        }


                    });
                    confermaDatiDialog.show(getFragmentManager(), CONFERMA_DATI_DIALOG);

                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void insertArticoloToDB(Articolo articolo) {
        Log.i(TAG, this.getClass().toString() + " insertArticoloToDB | inserisco " + articolo.toString() + " nel DB");
        ref.child(tipoAritcolo.toLowerCase() + "/" + eIdArticolo.getText().toString() + "/").setValue(articolo);
        List<String> list = new ArrayList<>();
        list = (List<String>) InternalStorage.readObject(getContext(),"id"+tipoAritcolo.toLowerCase());
        list.add(eIdArticolo.getText().toString());
        InternalStorage.writeObject(getContext(),"id"+tipoAritcolo.toLowerCase(),list);

    }

    public boolean checkCampo(EditText editText) {

        if (editText.getText().toString().equals(""))
            return false;
        return true;
    }

    private void setSpinner(Spinner tipoArticolo) {
        List<String> listaAritcoli = new ArrayList<>();
        listaAritcoli.add("Selezionare Tipo");
        listaAritcoli.add(ATTREZZI.toUpperCase());
        listaAritcoli.add(MATERIALI.toUpperCase());


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_white_item, R.id.spinnerRegistration, listaAritcoli);
        spinnerAddProduct.setAdapter(spinnerAdapter);
    }

    private void setIdArticolo(String tipo) {

        if (!spinnerAddProduct.getSelectedItem().toString().equalsIgnoreCase("Selezionare Tipo")){
        List<String> list = (List<String>) InternalStorage.readObject(getContext(), "id" + tipo.toLowerCase());
        if (list != null && list.size() > 0) {
            List<Integer> listInt = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                listInt.add(Integer.parseInt(list.get(i).substring(1)));
            }
            Collections.sort(listInt);
            eIdArticolo.setText((spinnerAddProduct.getSelectedItem().toString().equalsIgnoreCase(ATTREZZI)? "a":"m")+String.valueOf(listInt.get(listInt.size() - 1) + 1));

        } else {
            eIdArticolo.setText((spinnerAddProduct.getSelectedItem().toString().equalsIgnoreCase(ATTREZZI)? "a":"m")+ 1);
        }
        eIdArticolo.setFocusable(false);
        }
    }

}
