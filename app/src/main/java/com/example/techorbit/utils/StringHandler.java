package com.example.techorbit.utils;

import android.util.Log;

public class StringHandler {


    public String download_receipt_text(String orginal_text){
        String treated_text = null;
        StringBuilder s_builder = new StringBuilder();
        try{
            int c_len = orginal_text.length();
            int stone = 10;
            int bal = stone-c_len;

            s_builder.append(orginal_text);
            for (int i=0; i<bal; i++){
                s_builder.append(" ");
            }
            treated_text = s_builder.toString();
        }catch (Exception e){

        }

        return treated_text;
    }

    public String custom_download_receipt_text(String orginal_text, int stone){
        String treated_text = null;
        StringBuilder s_builder = new StringBuilder();
        try{
            int c_len = orginal_text.length();
            int bal = stone-c_len;

            s_builder.append(orginal_text);
            for (int i=0; i<bal; i++){
                s_builder.append(" ");
            }
            treated_text = s_builder.toString();
        }catch (Exception e){

        }

        return treated_text;
    }

    public String stock_receipt_text(String orginal_text){
        String treated_text = null;
        StringBuilder s_builder = new StringBuilder();
        try{
            int c_len = orginal_text.length();
            int stone = 12;
            int bal = stone-c_len;

            s_builder.append(orginal_text);
            for (int i=0; i<bal; i++){
                s_builder.append(" ");
            }
            treated_text = s_builder.toString();
        }catch (Exception e){

        }

        return treated_text;
    }

    public String twoDecimalsFormat(){
        String ret_val = null;
        try{

        }catch (Exception e){
            Log.e("Exception", e.getMessage());
        }
        return ret_val;
    }
}
