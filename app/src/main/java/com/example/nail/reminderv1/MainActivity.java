package com.example.nail.reminderv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private EditText mEditTextName;
    private GroceryAdapter mAdapter;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper dbHelper = new DBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = findViewById(R.id.recycler_view); //объявляем лэйаут
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GroceryAdapter(this, getAllItems()); //получает все данные из таблицы
        recyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) { //удаляем записи свайпом
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView); //прикрепляет данный метод к recyclerview

        mEditTextName = findViewById(R.id.edit_text);

        ImageButton buttonAdd = findViewById(R.id.add_button);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

    }

    private void addItem() { //функция размещения написанного текста в БД
        if (mEditTextName.getText().toString().trim().length() == 0) {
            return;
        }

        String name = mEditTextName.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, name);

        mDatabase.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, cv);
        mAdapter.swapCursor(getAllItems());

        mEditTextName.getText().clear();
    }
//удаляем из базы значение полученное свайпом в айди
    private void removeItem(long id){
        mDatabase.delete(GroceryContract.GroceryEntry.TABLE_NAME,
                GroceryContract.GroceryEntry._ID + "=" + id, null);
        mAdapter.swapCursor(getAllItems()); //возвразаем курсор на место
    }

    private Cursor getAllItems() {
        return mDatabase.query(
                GroceryContract.GroceryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                GroceryContract.GroceryEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }


}
