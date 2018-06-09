package com.example.chaithra.pinc.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.chaithra.pinc.R;
import com.example.chaithra.pinc.model.Question;
import java.io.InputStream;

public class QuestionAdapter extends ArrayAdapter<Question> {
    public QuestionAdapter(Context context, int resource) {

        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Question item = getItem(position);
        ViewHolder holder;
        if (v == null) {
            holder = new ViewHolder();
            v = LayoutInflater.from(getContext()).inflate(R.layout.activity_list, parent, false);
            holder.avatar = (ImageView) v.findViewById(R.id.avatar);
            holder.avatar1 = (ImageView) v.findViewById(R.id.avatar1);
            holder.avatar2 = (ImageView) v.findViewById(R.id.avatar2);
            holder.avatar3 = (ImageView) v.findViewById(R.id.avatar3);
            holder.imgBookmark = (ImageView) v.findViewById(R.id.img_bookmark_id);
            holder.question = (TextView) v.findViewById(R.id.txt_question_id);
            holder.postedUser = (TextView) v.findViewById(R.id.txt_posteduser_id);
            holder.emjSmile = (TextView) v.findViewById(R.id.txt_emj_smile_id);
            holder.emjThnik = (TextView) v.findViewById(R.id.txt_emj_think_id);
            holder.emjCry = (TextView) v.findViewById(R.id.txt_emj_cry_id);
            holder.emjAngry = (TextView) v.findViewById(R.id.txt_emj_angry_id);


            v.setTag(holder);

        } else {
            // view already exists, get the holder instance from the view
            holder = (ViewHolder) v.getTag();
        }
        holder.avatar.setImageResource(R.drawable.circle);
        if (item.getUser1() != null) {
            new DownloadImageTask(holder.avatar).execute(item.getUser1());
        }
        holder.avatar1.setImageResource(R.drawable.circle);
        if (item.getUser2() != null) {
            new DownloadImageTask(holder.avatar1).execute(item.getUser2());
        }
        holder.avatar2.setImageResource(R.drawable.circle);
        if (item.getUser3() != null) {
            new DownloadImageTask(holder.avatar2).execute(item.getUser3());
        }

//        holder.avatar3.setText(item.getAvatar3());//remainging commenters.

        holder.question.setText(item.getQuestion());
        holder.imgBookmark.setImageResource(item.isBookmarked() ? R.drawable.bookmark_enabled : R.drawable.bookmark);
        holder.postedUser.setText(item.getAuthor_name());
        String smileemj = new String(Character.toChars(0x1F60A));
        holder.emjSmile.setText(smileemj + " 1");

        String thinkemj = new String(Character.toChars(0x1F914));
        holder.emjThnik.setText(thinkemj + " 2");

        String cryemj = new String(Character.toChars(0x1F622));
        holder.emjCry.setText(cryemj + " 3");

        String angryemj = new String(Character.toChars(0x1F624));
        holder.emjAngry.setText(angryemj + " 4");

        return v;
    }

    private class ViewHolder {
        ImageView image;
        TextView question;
        ImageView imgBookmark;
        TextView postedUser;
        TextView emjSmile;
        TextView emjThnik;
        TextView emjCry;
        TextView emjAngry;
        ImageView avatar;
        ImageView avatar1;
        ImageView avatar2;
        ImageView avatar3;


    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}


