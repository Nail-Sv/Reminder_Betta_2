package com.example.nail.reminderv1;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public GroceryAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        //public TextView countText;
        public TextView idText; //добавляю переменную для отображения айди яйчейки базы данных


        public GroceryViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.name_item);
            idText = itemView.findViewById(R.id.text_id); //;объявляю поле для вывода айди текста

        }
    }

    @NonNull
    @Override
    public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.grocery_item, parent, false);
        return new GroceryViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)){ //проверка того что курсор на позиции
           return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_NAME)); //получает данные с курсора в стринг
        String id_name = mCursor.getString(mCursor.getColumnIndex(GroceryContract.GroceryEntry._ID)); //;получаем номер строки, но это не точно

        long id = mCursor.getLong(mCursor.getColumnIndex(GroceryContract.GroceryEntry._ID)); //получаем id яйчейки для послед удаления
      //  long id_name = mCursor.getString(mCursor.getColumnIndex(GroceryContract.GroceryEntry._ID)); //;получаем id яйчейки для последующего удаления


        holder.nameText.setText(name);
        holder.itemView.setTag(id);
        //* удерживаю айди текста
        holder.idText.setText(id_name);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if (mCursor != null){
            mCursor.close();
        }

        mCursor = newCursor;
        if (newCursor != null){
            notifyDataSetChanged();
        }

    }
}
