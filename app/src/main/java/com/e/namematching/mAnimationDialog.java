package com.e.namematching;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class mAnimationDialog {

    public static Context context_full;
    public static View v;
    public static Dialog dialog;
    public static LayoutInflater inflater;
    public static Button Red_BTN ;
    public static Button Green_BTN;
    public static ImageView img;
    public static TextView txt,titleb;
    public static RelativeLayout parent;
    public static boolean isgride;
    public static Button[] init(Context context, boolean isGrideViewButton){
        isgride=isGrideViewButton;
        context_full=context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialog = new Dialog(context);
        if (isGrideViewButton){
            v = inflater.inflate(R.layout.animation_dialog_gride_b, null);
        }else {
            v = inflater.inflate(R.layout.animation_dialog, null);
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(v);
        Red_BTN =dialog.findViewById(R.id.btn1);
        parent =dialog.findViewById(R.id.parent);
        Green_BTN =dialog.findViewById(R.id.btn2);
        img =dialog.findViewById(R.id.img);
        txt =dialog.findViewById(R.id.txt);
        titleb =dialog.findViewById(R.id.title);
        return new Button[]{Red_BTN,Green_BTN};
    }

    public static Dialog create(String title, String msg, String button_msg){

        titleb.setText(title);
        txt.setText(msg);
        Red_BTN.setText(button_msg);
        return dialog;
    }
    public static void change_text_color(String color){
        txt.setTextColor(Color.parseColor(color));
        titleb.setTextColor(Color.parseColor(color));
    }
    public static void custom_button_background(String color_btn){
        Red_BTN.setBackgroundColor(Color.parseColor(color_btn));
    }
    public static void custom_button_background(Drawable drawable){
        Red_BTN.setBackground(drawable);
    }
    public static void custom_button_background(int resid){
        Red_BTN.setBackgroundResource(resid);
    }

    public static void close(){
        dialog.dismiss();
    }
    public static void show(){
        dialog.show();
    }
    public static void setCanceledOnTouchOutside(boolean setCanceledOnTouchOutside){
        dialog.setCanceledOnTouchOutside(setCanceledOnTouchOutside);
    }
    public static ImageView set_image(Drawable drawable){
        if (img.getVisibility()== View.GONE){
            img.setVisibility(View.VISIBLE);
        }
        img.setImageDrawable(drawable);
return img;
    }
    public static ImageView set_image(Bitmap bitmap){
        if (img.getVisibility()== View.GONE){
            img.setVisibility(View.VISIBLE);
        }
        img.setImageBitmap(bitmap);
        return img;
    }
    public static ImageView set_image(int resid){
        if (img.getVisibility()== View.GONE){
            img.setVisibility(View.VISIBLE);
        }
        img.setImageResource(resid);
        return img;
    }
    public static void set_parent_background(String color){
        parent.setBackgroundColor(Color.parseColor(color));
    }
    public static MediaPlayer set_Sound(int sound_row_resid){
        MediaPlayer mp = MediaPlayer.create(context_full,sound_row_resid);
        mp.start();
        return mp;
    }
}
