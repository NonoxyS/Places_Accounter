package com.example.placesaccounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.placesaccounter.db.DbManager;
import com.example.placesaccounter.listAdapter.MainAdapter;
import com.example.placesaccounter.models.ModelLearner;
import com.example.placesaccounter.models.ModelRoom;
import com.example.placesaccounter.listAdapter.OnClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private DbManager dbManager;
    private RecyclerView rcView;
    private MainAdapter mainAdapter;
    private String selectedFloor = "";
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        OnClickListener clickListener = new OnClickListener() { // RecyclerView Listener
            @Override
            public void onItemClick(ModelRoom modelRoom, int position) {
                showEditDialog(modelRoom, position);
            }
        };



        dbManager = new DbManager(this);
        mainAdapter = new MainAdapter(this, clickListener);

        rcView = findViewById(R.id.rcView);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        rcView.setAdapter(mainAdapter);

        initFloorFilter();
        initSearchRoom();
    }

    private void showEditDialog(ModelRoom modelRoom, int position) {
        Dialog editDialog = new Dialog(this);
        editDialog.setContentView(R.layout.edit_item_dialog);
        editDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Map<String, Map<Integer, Integer>> editDialogIds = createEditDialogElements(editDialog, modelRoom);

        Button saveButton = editDialog.findViewById(R.id.saveBtn);
        Button cancelButton = editDialog.findViewById(R.id.cancelBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialogSaveBtn(editDialog, editDialogIds, modelRoom, position);
                editDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.cancel();
            }
        });
        editDialog.show();
    }

    private Map<String, Map<Integer, Integer>> createEditDialogElements(Dialog editDialog, ModelRoom modelRoom) {
        final float scaleDisplay = getResources().getDisplayMetrics().density; // Display metrics
        // (int) (<neededPixels> * scaleDisplay + 0.5f)

        Map<String, Map<Integer, Integer>> generatedIds = new HashMap<>();
        Map<Integer, Integer> streamNumberIds = new HashMap<>();
        Map<Integer, Integer> checkInDateIds = new HashMap<>();
        Map<Integer, Integer> checkOutDateIds = new HashMap<>();

        LinearLayout editContainer = editDialog.findViewById(R.id.EditContainer);

        for (int i = 0; i < modelRoom.getBeds_number(); i++) {
            LinearLayout residentInfoContainer = new LinearLayout(this);
            residentInfoContainer.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, (int) (48 * scaleDisplay + 0.5f)));
            residentInfoContainer.setOrientation(LinearLayout.HORIZONTAL);

            TextView rowNumber = new TextView(this);
            rowNumber.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            rowNumber.setText(new StringBuilder().append(i + 1).append(":").toString());
            rowNumber.setGravity(Gravity.CENTER_VERTICAL);

            EditText streamNumberEditText = new EditText(this);
            streamNumberEditText.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            streamNumberEditText.setHint(R.string.stream_number);
            streamNumberEditText.setGravity(Gravity.CENTER);
            streamNumberIds.put(i, View.generateViewId());
            streamNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            streamNumberEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
            streamNumberEditText.setId(streamNumberIds.get(i));

            EditText checkInDateEditText = new EditText(this);
            checkInDateEditText.setLayoutParams(new LinearLayout.LayoutParams
                    ((int) (120 * scaleDisplay + 0.5f), LinearLayout.LayoutParams.MATCH_PARENT));
            checkInDateEditText.setHint(R.string.check_in_date);
            checkInDateEditText.setClickable(false);
            checkInDateEditText.setFocusable(false);
            checkInDateEditText.setGravity(Gravity.CENTER);
            selectDate(checkInDateEditText);
            checkInDateIds.put(i, View.generateViewId());
            checkInDateEditText.setId(checkInDateIds.get(i));

            EditText checkOutDateEditText = new EditText(this);
            checkOutDateEditText.setLayoutParams(new LinearLayout.LayoutParams
                    ((int) (120 * scaleDisplay + 0.5f), LinearLayout.LayoutParams.MATCH_PARENT));
            checkOutDateEditText.setHint(R.string.check_out_date);
            checkOutDateEditText.setClickable(false);
            checkOutDateEditText.setFocusable(false);
            checkOutDateEditText.setGravity(Gravity.CENTER);
            selectDate(checkOutDateEditText);
            checkOutDateIds.put(i, View.generateViewId());
            checkOutDateEditText.setId(checkOutDateIds.get(i));

            ImageButton deleteResidentInfoBtn = new ImageButton(this);
            deleteResidentInfoBtn.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, (int) (30 * scaleDisplay + 0.5f)));
            deleteResidentInfoBtn.setBackgroundResource(R.drawable.roundcorner);
            deleteResidentInfoBtn.setImageResource(android.R.drawable.ic_menu_delete);
            ((LinearLayout.LayoutParams) deleteResidentInfoBtn.getLayoutParams()).gravity = Gravity.CENTER;

            deleteResidentInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    streamNumberEditText.setText("");
                    checkInDateEditText.setText("");
                    checkOutDateEditText.setText("");
                }
            });


            if (modelRoom.getLearners_in_room().size() > i) {
                streamNumberEditText.setText(String.valueOf(modelRoom.getLearners_in_room().get(i).getStream_number()));
                checkInDateEditText.setText(String.valueOf(modelRoom.getLearners_in_room().get(i).getCheck_in_date()));
                checkOutDateEditText.setText(String.valueOf(modelRoom.getLearners_in_room().get(i).getCheck_out_date()));
            }

            residentInfoContainer.addView(rowNumber);
            residentInfoContainer.addView(streamNumberEditText);
            residentInfoContainer.addView(checkInDateEditText);
            residentInfoContainer.addView(checkOutDateEditText);
            residentInfoContainer.addView(deleteResidentInfoBtn);

            editContainer.addView(residentInfoContainer);
        }
        generatedIds.put("streamNumberIds", streamNumberIds);
        generatedIds.put("checkInDateIds", checkInDateIds);
        generatedIds.put("checkOutDateIds", checkOutDateIds);
        return generatedIds;
    }

    private void selectDate(EditText datePicker) {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myDateFormat = "dd-MM-yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myDateFormat, Locale.US);

                datePicker.setText(dateFormat.format(myCalendar.getTime()));
            }
        };

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void editDialogSaveBtn(Dialog editDialog, Map<String, Map<Integer, Integer>> editDialogIds,
                                   ModelRoom modelRoom, int position) {

        ArrayList<Integer> indexesForDeleting = new ArrayList<>(modelRoom.getLearners_in_room().size());

        for (int i = 0; i < modelRoom.getBeds_number(); i++) {
            EditText streamNumberET = editDialog.findViewById(editDialogIds.get("streamNumberIds").get(i));
            EditText checkInDateET = editDialog.findViewById(editDialogIds.get("checkInDateIds").get(i));
            EditText checkOutDateET = editDialog.findViewById(editDialogIds.get("checkOutDateIds").get(i));

            if (modelRoom.getLearners_in_room().size() > i //There is a learner and there has been a change in his data
                    && checkIfLearnerInfoChanged(editDialog, editDialogIds, modelRoom.getLearners_in_room().get(i), i)) {

                handleEditedLearner(streamNumberET, checkInDateET, checkOutDateET, modelRoom, indexesForDeleting, i, position);

            } else if (modelRoom.getLearners_in_room().size() < i + 1) {
                handleAddedLearner(streamNumberET, checkInDateET, checkOutDateET, modelRoom, position);
            }
        }
        removeDeletedLearners(modelRoom, indexesForDeleting);
    }

    private void handleEditedLearner(EditText streamNumberET, EditText checkInDateET, EditText checkOutDateET,
                                     ModelRoom modelRoom, ArrayList<Integer> indexesForDeleting,
                                     int indexLearner, int position) {

        if (streamNumberET.getText().toString().isEmpty() &&
                checkInDateET.getText().toString().isEmpty() &&
                checkOutDateET.getText().toString().isEmpty()) {

            deleteLearnerAndUpdateAdapter(modelRoom, indexesForDeleting, indexLearner, position);
        }
        else if (!streamNumberET.getText().toString().isEmpty() &&
                !checkInDateET.getText().toString().isEmpty() &&
                !checkOutDateET.getText().toString().isEmpty()) {

            updateLearnerAndUpdateAdapter(modelRoom, streamNumberET, checkInDateET, checkOutDateET,
                    indexLearner, position);
        }
    }

    private void handleAddedLearner(EditText streamNumberET, EditText checkInDateET, EditText checkOutDateET,
                                    ModelRoom modelRoom, int position) {
        if (!streamNumberET.getText().toString().isEmpty() &&
                !checkInDateET.getText().toString().isEmpty() &&
                !checkOutDateET.getText().toString().isEmpty()) {

            long _id = dbManager.insertToDb(modelRoom.getRoom_number(),
                    Integer.parseInt(streamNumberET.getText().toString()),
                    checkInDateET.getText().toString(),
                    checkOutDateET.getText().toString());

            addNewLearnerAndUpdateAdapter(modelRoom, streamNumberET, checkInDateET, checkOutDateET, _id, position);
        }
    }

    private void removeDeletedLearners(ModelRoom modelRoom, ArrayList<Integer> indexesForDeleting) {
        indexesForDeleting.sort(Collections.reverseOrder());
        for (int i : indexesForDeleting) {
            modelRoom.getLearners_in_room().remove(i);
        }
    }

    private void deleteLearnerAndUpdateAdapter(ModelRoom modelRoom, ArrayList<Integer> indexesForDeleting,
                                               int indexForDeleting, int position) {
        dbManager.deleteFromDb(modelRoom.getLearners_in_room().get(indexForDeleting).get_id());
        indexesForDeleting.add(indexForDeleting);
        mainAdapter.updateAdapter(modelRoom, position);
        calculatePlaces(dbManager.readFromDb("", selectedFloor));
    }

    private void addNewLearnerAndUpdateAdapter(ModelRoom modelRoom, EditText streamNumberET, EditText checkInDateET,
                                               EditText checkOutDateET, long _id, int position) {
        modelRoom.getLearners_in_room().add(new ModelLearner(_id,
                modelRoom.getRoom_number(),
                Integer.parseInt(streamNumberET.getText().toString()),
                checkInDateET.getText().toString(),
                checkOutDateET.getText().toString()));

        mainAdapter.updateAdapter(modelRoom, position);
        calculatePlaces(dbManager.readFromDb("", selectedFloor));
    }

    private void updateLearnerAndUpdateAdapter(ModelRoom modelRoom, EditText streamNumberET, EditText checkInDateET,
                                               EditText checkOutDateET, int indexLearner, int position) {
        modelRoom.getLearners_in_room().get(indexLearner).
                setStream_number(Integer.parseInt(streamNumberET.getText().toString()));
        modelRoom.getLearners_in_room().get(indexLearner).
                setCheck_in_date(checkInDateET.getText().toString());
        modelRoom.getLearners_in_room().get(indexLearner).
                setCheck_out_date(checkOutDateET.getText().toString());

        dbManager.updateDbEntry(Integer.parseInt(streamNumberET.getText().toString()),
                checkInDateET.getText().toString(),
                checkOutDateET.getText().toString(),
                modelRoom.getLearners_in_room().get(indexLearner).get_id());
        mainAdapter.updateAdapter(modelRoom, position);
    }

    private boolean checkIfLearnerInfoChanged(Dialog editDialog, Map<String, Map<Integer, Integer>> editDialogIds,
                                              ModelLearner modelLearner, int rowNumber) {
        EditText streamNumberET = editDialog.findViewById(editDialogIds.get("streamNumberIds").get(rowNumber));
        EditText checkInDateET = editDialog.findViewById(editDialogIds.get("checkInDateIds").get(rowNumber));
        EditText checkOutDateET = editDialog.findViewById(editDialogIds.get("checkOutDateIds").get(rowNumber));

        return !String.valueOf(modelLearner.getStream_number()).equals(streamNumberET.getText().toString().trim()) ||
                !String.valueOf(modelLearner.getCheck_in_date()).equals(checkInDateET.getText().toString().trim()) ||
                !String.valueOf(modelLearner.getCheck_out_date()).equals(checkOutDateET.getText().toString().trim());
    }

private void initSearchRoom() {
    androidx.appcompat.widget.SearchView searchView = findViewById(R.id.searchView);
    searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

    EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
    editText.setHint("Поиск");
    editText.setTextColor(getColor(R.color.deactive));
    editText.setTextSize(14);

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            searchView.clearFocus();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            mainAdapter.updateAdapter(dbManager.readFromDb(newText, selectedFloor));
            return false;
        }
    });
}

private void initFloorFilter() {
    Spinner floorFilterSpinner = findViewById(R.id.floorFilter);

    ArrayAdapter<CharSequence> floorFilterAdapter = ArrayAdapter.createFromResource(this,
            R.array.spinner_items,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

    floorFilterAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
    floorFilterSpinner.setAdapter(floorFilterAdapter);

    floorFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String[] floorNumbers = {"", "3", "4", "5", "6", "7", "8"};
            selectedFloor = floorNumbers[position];
            mainAdapter.updateAdapter(dbManager.readFromDb("", selectedFloor));
            calculatePlaces(dbManager.readFromDb("", selectedFloor));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });
}

private void calculatePlaces(List<ModelRoom> roomList) {
    TextView tv_availablePlaces = findViewById(R.id.availablePlacesTV);
    TextView tv_totalPlaces = findViewById(R.id.totalPlacesTV);

    int availablePlaces = 0, totalPlaces = 0;

    for (ModelRoom modelRoom : roomList) {
        totalPlaces += modelRoom.getBeds_number();
        availablePlaces += modelRoom.getBeds_number() - modelRoom.getLearners_in_room().size();
    }

    tv_availablePlaces.setText(new StringBuilder().append("Свободно: ").
            append(availablePlaces).append(" ").append(placesDeclension(availablePlaces)));

    tv_totalPlaces.setText(new StringBuilder().
            append("Всего: ").append(totalPlaces).append(" ").append(placesDeclension(totalPlaces)));
}

private String placesDeclension(int places) {
    if (places >= 10 && places <= 20)
        return "мест";

    switch (places % 10) {
        case 1: return " место";
        case 2:
        case 3:
        case 4:
            return "места";
        default: return "мест";
    }
}

    @Override
    protected void onResume() {
        super.onResume();
        dbManager.openDb();
        mainAdapter.updateAdapter(dbManager.readFromDb("", selectedFloor));
        calculatePlaces(dbManager.readFromDb("", selectedFloor));
    }

    public void goToSecondActivity(View v) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);

    }

    public void goToThirdActivity(View v) {
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }

    public void goToFourthActivity(View v) {
        Intent intent = new Intent(this, FourthActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.closeDb();
    }
}