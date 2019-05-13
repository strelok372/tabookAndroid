package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class NumbersAdapter extends RecyclerView.Adapter<NumbersAdapter.myViewHolder>{

    String name;
    String tags;
    Long id;
    private Context mContext; //контекст нашего главного приложения
    private Cursor mCursor; //курсор для перемещения по таблице

    NumbersAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //создание элементов, ограниченное количество
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.number_list_item, viewGroup, false);
        return new myViewHolder(view);
    }

    @Override //обновляем значение здесь
    public void onBindViewHolder(@NonNull myViewHolder viewHolder, int i) {
        if (!mCursor.moveToPosition(i)){
            return;
        }
        //получаем значения нужных столбцов по курсору
        name = mCursor.getString(mCursor.getColumnIndex(RecordsContract.RecordEntry.KEY_NAME));
        tags = mCursor.getString(mCursor.getColumnIndex(RecordsContract.RecordEntry.KEY_TAGS));
        id = mCursor.getLong(mCursor.getColumnIndex(RecordsContract.RecordEntry.ID));
        //присваиваем значения элементам списка
        viewHolder.recordName.setText(name);
        viewHolder.recordTags.setText(tags);
        viewHolder.itemView.setTag(id);
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

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView recordName;
        TextView recordTags;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            recordName = itemView.findViewById(R.id.tv_record_name);
            recordTags = itemView.findViewById(R.id.tv_tag_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext , ShowRecord.class);
                    intent.putExtra("adapterpos", getAdapterPosition());
                    mContext.startActivity(intent);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    getAdapterPosition();
                    v.showContextMenu();
                    return true;
                }
            });
        }

        void bind(int listIndex){
            recordTags.setText(String.valueOf(listIndex));
        }
    }
}
