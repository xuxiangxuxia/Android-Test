package com.bistu.firstexperiment;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edit_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton button_1 = findViewById(R.id.button_1);
        button_1.setOnClickListener(this);

        ImageButton button_2 = findViewById(R.id.button_2);
        button_2.setOnClickListener(this);
        ImageButton button_3 = findViewById(R.id.button_3);
        button_3.setOnClickListener(this);
        ImageButton button_4 = findViewById(R.id.button_4);
        button_4.setOnClickListener(this);
        ImageButton button_5 = findViewById(R.id.button_5);
        button_5.setOnClickListener(this);
        ImageButton button_6 = findViewById(R.id.button_6);
        button_6.setOnClickListener(this);
        ImageButton button_7 = findViewById(R.id.button_7);
        button_7.setOnClickListener(this);
        ImageButton button_8 = findViewById(R.id.button_8);
        button_8.setOnClickListener(this);
        ImageButton button_9 = findViewById(R.id.button_9);
        button_9.setOnClickListener(this);
        ImageButton button_0 = findViewById(R.id.button_0);
        button_0.setOnClickListener(this);
        ImageButton button_point = findViewById(R.id.button_point);
        button_point.setOnClickListener(this);
        ImageButton button_add = findViewById(R.id.button_add);
        button_add.setOnClickListener(this);
        ImageButton button_sub = findViewById(R.id.button_sub);
        button_sub.setOnClickListener(this);
        ImageButton button_mul = findViewById(R.id.button_mul);
        button_mul.setOnClickListener(this);
        ImageButton button_div = findViewById(R.id.button_div);
        button_div.setOnClickListener(this);
        ImageButton button_delete = findViewById(R.id.button_del);
        button_delete.setOnClickListener(this);
        ImageButton button_square = findViewById(R.id.button_sqrt);
        button_square.setOnClickListener(this);
        Button button_equals = findViewById(R.id.button_equal);
        button_equals.setOnClickListener(this);
        Button button_clear = findViewById(R.id.button_clear);
        button_clear.setOnClickListener(this);
        edit_text = findViewById(R.id.editText_1);
    }

    @Override
    public void onClick(View view) {
        String output = edit_text.getText().toString();
        switch (view.getId()) {
            case R.id.button_1:
                edit_text.setText(output + "1");
                break;
            case R.id.button_2:
                edit_text.setText(output + "2");
                break;
            case R.id.button_3:
                edit_text.setText(output + "3");
                break;
            case R.id.button_4:
                edit_text.setText(output + "4");
                break;
            case R.id.button_5:
                edit_text.setText(output + "5");
                break;
            case R.id.button_7:
                edit_text.setText(output + "7");
                break;
            case R.id.button_6:
                edit_text.setText(output + "6");
                break;
            case R.id.button_8:
                edit_text.setText(output + "8");
                break;
            case R.id.button_9:
                edit_text.setText(output + "9");
                break;
            case R.id.button_0:
                edit_text.setText(output + "0");
                break;
            case R.id.button_add:
                edit_text.setText(output + "+");
                if(edit_text.getText().length()==1){
                    Toast.makeText(this,"???????????????????????????",Toast.LENGTH_SHORT).show();
                    delete();
                }
                break;
            case R.id.button_sub:
                edit_text.setText(output + "-");
                break;
            case R.id.button_mul:
                edit_text.setText(output + "*");
                if(edit_text.getText().length()==1){
                    Toast.makeText(this,"???????????????????????????",Toast.LENGTH_SHORT).show();
                    delete();
                }
                break;
            case R.id.button_div:
                edit_text.setText(output + "/");
                if(edit_text.getText().length()==1){
                    Toast.makeText(this,"???????????????????????????",Toast.LENGTH_SHORT).show();
                    delete();
                }
                break;
            case R.id.button_equal:
                check();
                break;
            case R.id.button_del:
                delete();
                break;
            case R.id.button_clear:
                edit_text.setText(null);
                break;
            case R.id.button_point:
                edit_text.setText(output + ".");
                break;
            case R.id.button_sqrt:
                edit_text.setText(output+"???");
                break;
        }
    }


    //????????????????????????????????????????????????
    private void delete(){
        if(edit_text.getText().length()==0){
            Toast.makeText(this,"?????????",Toast.LENGTH_SHORT).show();
            edit_text.setText("0");
        }

        String str = edit_text.getText().toString();
        char strs[] = str.toCharArray();
        int a = strs.length - 1;
        edit_text.setText(strs,0,a);
    }


    //??????????????????????????????????????????????????????????????????????????????
    private boolean check(){

        String str = edit_text.getText().toString();
        char strs[] = str.toCharArray();
        int a = strs.length;
        ArrayList<Integer> sign_1 = new ArrayList<Integer>();

        //????????????char?????????????????????????????????
        for (int i = 0; i < a; i++) {
            if (strs[i] == '+' || strs[i] == '-' || strs[i] == '*' || strs[i] == '/') {
                sign_1.add(i);
            }
        }


        //?????????????????????0
       for (int i = 0; i < a-1; i++) {
            if(strs[i]=='/' && strs[i+1]=='0'){
                Toast.makeText(this, "???????????????0", Toast.LENGTH_SHORT).show();
                return false;
            }
            //??????????????????????????????
            if(strs[i] == '???' && strs[i+1] == '-'){
                Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show();
                return false;
            }
        }


       //????????????????????????????????????????????????????????????
        for(int j = 0;j<sign_1.size()-1;j++) {
            if (((sign_1.get(j) + 1) == sign_1.get(j + 1)) && (strs[sign_1.get(j + 1)] != '-')) {
                Toast.makeText(this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        //????????????????????????????????????
        if(strs[strs.length-1]=='+'||strs[strs.length-1]=='-'||strs[strs.length-1]=='*'||strs[strs.length-1]=='/'||strs[strs.length-1]=='???'){
            Toast.makeText(this, "???????????????????????????", Toast.LENGTH_SHORT).show();
            return false;
        }

        result();
        return true;
    }


    //????????????
   private void result() {

       String str = edit_text.getText().toString();
       char strs[] = str.toCharArray();
       int a = strs.length;

       //???????????????????????????String???????????????
       ArrayList<String> number = new ArrayList<>();

       String number_s = "";

       //?????????????????????char???????????????
       ArrayList<Integer> sign_1 = new ArrayList<Integer>();
       for (int i = 0; i < a; i++) {
           if (strs[i] == '+' || strs[i] == '-' || strs[i] == '*' || strs[i] == '/') {
               sign_1.add(i);
           }
       }

       //??????????????????????????????????????????
       for(int j = 0;j<sign_1.size()-1;j++) {
       if(((sign_1.get(j) + 1) == sign_1.get(j + 1)) && (strs[sign_1.get(j + 1)] == '-')){
           sign_1.remove(j+1);
       }
       }

       int n = 0;
       int begin = 0;
       int end = 0;
       ArrayList<Character> sign_2 = new ArrayList<>();
       int l = 1;

       //?????????????????????????????????????????????n??????????????????????????????
       for (int k = 0; ; k++) {
             if (begin == end) {
                 if(begin>0){
                     begin++;

                     //?????????????????????
                     if( begin == strs.length+1){
                         break;
                     }
                 }
                 //?????????????????????????????????
                 if(n == sign_1.size()){
                     end = strs.length;
                 }
                 else{
                     end = sign_1.get(n);
                 }
                 //n?????????????????????
                 n++;
             } else {
                 for (; begin < end; begin++) {
                     number_s = number_s + strs[begin];
                     if (begin == (end - 1)) {
                         number.add(number_s);
                         number_s = "";
                         if( end != strs.length){
                             //???????????????????????????????????????
                             sign_2.add(strs[end]);
                         }
                     }
                 }
             }
       }

       //?????????????????????????????????float???????????????
     ArrayList<Float> number_1 = new ArrayList<Float>();
     for(int j=0;j<number.size();j++){
         char sqrt[] = number.get(j).toCharArray();
         String sq = "";

         //??????????????????????????????????????????????????????????????????????????????????????????
         if(sqrt[0]=='???'){
             for(int i = 1;i<sqrt.length;i++){
                 sq = sq + sqrt[i];
             }
             float s = Float.parseFloat(String.valueOf(Math.sqrt(Double.parseDouble(sq))));
             number_1.add(s);
         }
         else{
             number_1.add(Float.parseFloat(number.get(j)));
         }
     }

     //???????????????????????????????????????????????????
     int s = sign_2.size();

     //??????????????????????????????????????????????????????
     if(s==0){
         str = (""+number_1.get(0));
         edit_text.setText(str);
     }

     for(int j=0;j<s;j++){
         //???????????????????????????????????????????????????
         if(sign_2.contains('*')==true ||sign_2.contains('/')==true){
             for(int i=0;i< sign_2.size();i++){
                 if(sign_2.get(i)=='*'){
                     float b = number_1.get(i);
                     float c = number_1.get(i+1);
                     number_1.set(i,b*c);
                     number_1.remove(i+1);
                     if(sign_2.size()>1){
                         sign_2.remove(i);
                     }
                     break;
                 }
                  if(sign_2.get(i)=='/'){
                     float b = number_1.get(i);
                     float c = number_1.get(i+1);
                     number_1.set(i,b/c);
                     number_1.remove(i+1);
                     if(sign_2.size()>1){
                         sign_2.remove(i);
                     }
                     break;
                 }
             }
             if(number_1.size()==1){
                 str = (""+number_1.get(0));
                 edit_text.setText(str);
                 break;
             }
             continue;
         }
         //??????????????????????????????????????????
         else{
             if(sign_2.get(0)=='+'){
                 float b = number_1.get(0);
                 float c = number_1.get(1);
                 number_1.set(0,b+c);
                 number_1.remove(1);
                 if(sign_2.size()>1){
                     sign_2.remove(0);
                 }
             }
             if(sign_2.get(0)=='-'){
                 float b = number_1.get(0);
                 float c = number_1.get(1);
                 number_1.set(0,b-c);
                 number_1.remove(1);
                 if(sign_2.size()>1){
                     sign_2.remove(0);
                 }
             }
             if(number_1.size()==1){
                 str = (""+number_1.get(0));
                 edit_text.setText(str);
                 break;
             }
         }
     }
    }
}