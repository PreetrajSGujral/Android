package com.example.android.fitnessapp2;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class Suggestions extends AppCompatActivity {
private Session session;
    String email1,result,gender;
    double bmi,h,w,step;
    int age;
    TextView sug;
    SQLiteHelper sqLiteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        session= new Session(this);
        String email= session.getEmail();
        email1=email.toString();
        sug=(TextView)findViewById(R.id.sug);
        /*
        emailText=(TextView)findViewById(R.id.textemail);
        genderText=(TextView)findViewById(R.id.textgender);
        ageText=(TextView)findViewById(R.id.textage);
        heightText=(TextView)findViewById(R.id.textheight);
        weightText=(TextView)findViewById(R.id.textweight);
        stepText=(TextView)findViewById(R.id.textsteps);
        bmiText=(TextView)findViewById(R.id.textbmi);
        */


        sqLiteHelper = new SQLiteHelper(this);

        Cursor data = sqLiteHelper.getLastRow(email1);
        if (data.getCount() == 0) {
            Toast.makeText(this, "Database is empty", Toast.LENGTH_SHORT).show();

        } else {

            while (data.moveToNext()) {
              //  emailText.setText("Email: " + email1);
              //  genderText.setText("Gender: " + data.getString(3));
                gender=data.getString(3);
              //  ageText.setText("Age: " + data.getString(4));
                age=Integer.parseInt(data.getString(4));
              //  heightText.setText("Height: " + data.getString(5));
                //weightText.setText("Weight: " + data.getString(6));
                h=Double.parseDouble(data.getString(5));
                w=Double.parseDouble(data.getString(6));
                bmi=w/(h*h);
               // bmiText.setText("BMI: " +bmi);

            }
        }


        Cursor data1 = sqLiteHelper.getLastRowStep(email1);
        if (data1.getCount() == 0) {
            Toast.makeText(this, "Database is empty", Toast.LENGTH_SHORT).show();

        } else {

            while (data1.moveToNext()) {

              // stepText.setText("Steps: "+ data1.getString(2));
                step = Double.parseDouble(data1.getString(2));

            }
        }

        Cursor data2 = sqLiteHelper.getLastRowResult(email1);
        if (data2.getCount() == 0) {
            Toast.makeText(this, "Database is empty", Toast.LENGTH_SHORT).show();

        } else {

            while (data2.moveToNext()) {

               // stepText.setText("Steps: "+ data1.getString(2));
                 result=data2.getString(4);


//                sug.setText("Suggestions: "+result);



            }
        }

// proper, male
        if((age>=6 && age <=13)&&(result.equals("Proper Sleep"))&&(step>=13000)&&(gender.equals("Male"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Keep it up!!!Work on improving your diet. Plan your meals.");
        }
        else if((age>=6 && age <=13)&&(result.equals("Proper Sleep"))&&(step>=13000)&&(gender.equals("Male"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Keep it up!!!Your health is perfect");
        }
        else if((age>=6 && age <=13)&&(result.equals("Proper Sleep"))&&(step>=13000)&&(gender.equals("Male"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Keep it up!!!Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
//improper, male
        else if((age>=6 && age <=13)&&(result.equals("Improper Sleep"))&&(step>=13000)&&(gender.equals("Male"))&&(bmi<20)){
            //good step count && bad sleep but poor bmi(malnourished
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga.Work on improving your diet. Plan your meals");
        }
        else if((age>=6 && age <=13)&&(result.equals("Improper Sleep"))&&(step>=13000)&&(gender.equals("Male"))&&(bmi>20 && bmi<25)){
            //step count good, sleep bad, bmi good
            sug.setText("Improper Sleep!!!Everything looks good. Try to meditate and do some yoga. Work on keeping your mind relaxed. ");
        }
        else  if((age>=6 && age <=13)&&(result.equals("Improper Sleep"))&&(step>=13000)&&(gender.equals("Male"))&&(bmi>25)){
            //step count good, bmi overweight, sleep bad
            sug.setText("Improper Sleep!!! Try to meditate and do some yoga. Work on keeping your mind relaxed.Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
        //female,proper

        else if((age>=6 && age <=13)&&(result.equals("Proper Sleep"))&&(step>=11000)&&(gender.equals("Female"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Keep it up!!!Work on improving your diet. Plan your meals.");
        }
        else if((age>=6 && age <=13)&&(result.equals("Proper Sleep"))&&(step>=11000)&&(gender.equals("Female"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Keep it up!!!Your health is perfect");
        }
        else if((age>=6 && age <=13)&&(result.equals("Proper Sleep"))&&(step>=11000)&&(gender.equals("Female"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Keep it up!!!Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
//female, improper

        else   if((age>=6 && age <=13)&&(result.equals("Improper Sleep"))&&(step>=11000)&&(gender.equals("Female"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Improper Sleep!!! Try to meditate and do some yoga. Work on keeping your mind relaxed.Work on improving your diet. Plan your meals.");
        }
        else if((age>=6 && age <=13)&&(result.equals("Improper Sleep"))&&(step>=11000)&&(gender.equals("Female"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Improper Sleep!!! Try to meditate and do some yoga. Work on keeping your mind relaxed.Everything looks good. Stress less!");
        }
        else if((age>=6 && age <=13)&&(result.equals("Improper Sleep"))&&(step>=11000)&&(gender.equals("Female"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Improper Sleep!!! Try to meditate and do some yoga. Work on keeping your mind relaxed.Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }

        //bad step count, male
        else if((age>=6 && age <=13)&&(result.equals("Proper Sleep"))&&(step<13000)&&(gender.equals("Male"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Get more active. Try to take a small walk every 30 minutes!!!Work on improving your diet. Plan your meals.");
        }
        else if((age>=6 && age <=13)&&(result.equals("Proper Sleep"))&&(step<13000)&&(gender.equals("Male"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Get more active.Try to take a small walk every 30 minutes!!!Healthy diet.");
        }
        else if((age>=6 && age <=13)&&(result.equals("Proper Sleep"))&&(step<13000)&&(gender.equals("Male"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Get more active.  Try to take a small walk every 30 minutes!!! Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }

//improper, bad step
        else if((age>=6 && age <=13)&&(result.equals("Improper Sleep"))&&(step<13000)&&(gender.equals("Male"))&&(bmi<20)){
            //good step count && bad sleep but poor bmi(malnourished
            sug.setText("Improper Sleep!!! Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active.Try to take a small walk every 30 minutes!!!Work on improving your diet");
        }
        else if((age>=6 && age <=13)&&(result.equals("Improper Sleep"))&&(step<13000)&&(gender.equals("Male"))&&(bmi>20 && bmi<25)){
            //step count good, sleep bad, bmi good
            sug.setText("Improper Sleep!!! Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Work on improving your diet");
        }
        else if((age>=6 && age <=13)&&(result.equals("Improper Sleep"))&&(step<13000)&&(gender.equals("Male"))&&(bmi>25)){
            //step count good, bmi overweight, sleep bad
            sug.setText("Improper Sleep!!! Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active.  Try to take a small walk every 30 minutes!!!Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
//female, bad step
        else if((age>=6 && age <=13)&&(result.equals("Proper Sleep"))&&(step<11000)&&(gender.equals("Female"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Get more active.  Try to take a small walk every 30 minutes!!!Work on improving your diet. Plan your meals.");
        }
        else if((age>=6 && age <=13)&&(result.equals("Proper Sleep"))&&(step<11000)&&(gender.equals("Female"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Get more active. Try to take a small walk every 30 minutes!!!Healthy diet.");
        }
        else if((age>=6 && age <=13)&&(result.equals("Proper Sleep"))&&(step<11000)&&(gender.equals("Female"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Get more active. Try to take a small walk every 30 minutes!!!Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }

//improper, female, bad step
        else if((age>=6 && age <=13)&&(result.equals("Improper Sleep"))&&(step<11000)&&(gender.equals("Female"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Improper Sleep!!! Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active.Try to take a small walk every 30 minutes!!!Work on improving your diet. Plan your meals.");
        }
        else if((age>=6 && age <=13)&&(result.equals("Improper Sleep"))&&(step<11000)&&(gender.equals("Female"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Improper Sleep!!! Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Healthy diet.");
        }
        else if((age>=6 && age <=13)&&(result.equals("Improper Sleep"))&&(step<11000)&&(gender.equals("Female"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Improper Sleep!!! Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }


        //14-25
        else if((age>=14 && age <= 25)&&(result.equals("Proper Sleep"))&&(step>=10000)&&(gender.equals("Male"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Keep it up!!!"+"\n"+"Work on improving your diet. Plan your meals.");
        }
        else if((age>=14 && age <= 25)&&(result.equals("Proper Sleep"))&&(step>=10000)&&(gender.equals("Male"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Keep it up!!!"+"\n"+"Healthy diet");
        }
        else if((age>=14 && age <= 25)&&(result.equals("Proper Sleep"))&&(step>=10000)&&(gender.equals("Male"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Keep it up!!!"+"\n"+"Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
//improper, male
        else if((age>=14 && age <= 25)&&(result.equals("Improper Sleep"))&&(step>=10000)&&(gender.equals("Male"))&&(bmi<20)){
            //good step count && bad sleep but poor bmi(malnourished
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Work on improving your diet. Plan your meals.");
        }
        else  if((age>=14 && age <= 25)&&(result.equals("Improper Sleep"))&&(step>=10000)&&(gender.equals("Male"))&&(bmi>20 && bmi<25)){
            //step count good, sleep bad, bmi good
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Everything looks good. Stress less!");
        }
        else if((age>=14 && age <= 25)&&(result.equals("Improper Sleep"))&&(step>=10000)&&(gender.equals("Male"))&&(bmi>25)){
            //step count good, bmi overweight, sleep bad
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
        //female,proper

        else if((age>=14 && age <= 25)&&(result.equals("Proper Sleep"))&&(step>=10000)&&(gender.equals("Female"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Keep it up!!!"+"\n"+"Work on improving your diet. Plan your meals.");
        }
        else if((age>=14 && age <= 25)&&(result.equals("Proper Sleep"))&&(step>=10000)&&(gender.equals("Female"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Keep it up!!!"+"\n"+"Healthy diet.");
        }
        else  if((age>=14 && age <= 25)&&(result.equals("Proper Sleep"))&&(step>=10000)&&(gender.equals("Female"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Keep it up!!!"+"\n"+"Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
//female, improper

        else if((age>=14 && age <= 25)&&(result.equals("Improper Sleep"))&&(step>=10000)&&(gender.equals("Female"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Work on improving your diet. Plan your meals.");
        }
        else if((age>=14 && age <= 25)&&(result.equals("Improper Sleep"))&&(step>=10000)&&(gender.equals("Female"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Everything looks good. Stress less!");
        }
        else if((age>=14 && age <= 25)&&(result.equals("Improper Sleep"))&&(step>=10000)&&(gender.equals("Female"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }

        //bad step count, male
        else if((age>=14 && age <= 25)&&(result.equals("Proper Sleep"))&&(step<10000)&&(gender.equals("Male"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Get more active. Try to take a small walk every 30 minutes!!!Work on improving your diet. Plan your meals.");
        }
        else if((age>=14 && age <= 25)&&(result.equals("Proper Sleep"))&&(step<10000)&&(gender.equals("Male"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Get more active.Try to take a small walk every 30 minutes!!!Healthy diet");
        }
        else if((age>=14 && age <= 25)&&(result.equals("Proper Sleep"))&&(step<10000)&&(gender.equals("Male"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Get more active. Try to take a small walk every 30 minutes!!!Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
//improper, bad step
        else if((age>=14 && age <= 25)&&(result.equals("Improper Sleep"))&&(step<10000)&&(gender.equals("Male"))&&(bmi<20)){
            //good step count && bad sleep but poor bmi(malnourished
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Work on improving your diet. Plan your meals.");
        }
        else if((age>=14 && age <= 25)&&(result.equals("Improper Sleep"))&&(step<10000)&&(gender.equals("Male"))&&(bmi>20 && bmi<25)){
            //step count good, sleep bad, bmi good
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Healthy diet");
        }
        else if((age>=14 && age <= 25)&&(result.equals("Improper Sleep"))&&(step<10000)&&(gender.equals("Male"))&&(bmi>25)){
            //step count good, bmi overweight, sleep bad
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active.Try to take a small walk every 30 minutes!!!Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
//female, bad step
        else if((age>=14 && age <= 25)&&(result.equals("Proper Sleep"))&&(step<10000)&&(gender.equals("Female"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Get more active. Try to take a small walk every 30 minutes!!!Work on improving your diet. Plan your meals.");
        }
        else   if((age>=14 && age <= 25)&&(result.equals("Proper Sleep"))&&(step<10000)&&(gender.equals("Female"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Get more active.Try to take a small walk every 30 minutes!!!Healthy diet");
        }
        else if((age>=14 && age <= 25)&&(result.equals("Proper Sleep"))&&(step<10000)&&(gender.equals("Female"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Get more active.Try to take a small walk every 30 minutes!!!Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }

//improper, female, bad step
        else if((age>=14 && age <= 25)&&(result.equals("Improper Sleep"))&&(step<10000)&&(gender.equals("Female"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Work on improving your diet. Plan your meals.");
        }
        else  if((age>=14 && age <= 25)&&(result.equals("Improper Sleep"))&&(step<10000)&&(gender.equals("Female"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Healthy diet");
        }
        else if((age>=14 && age <= 25)&&(result.equals("Improper Sleep"))&&(step<10000)&&(gender.equals("Female"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }




        //adults 26-64

        else if((age>=26 && age<=64)&&(result.equals("Proper Sleep"))&&(step>=7000)&&(gender.equals("Male"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Keep it up!!!"+"\n"+"Work on improving your diet. Plan your meals.");
        }
        else if((age>=26 && age<=64)&&(result.equals("Proper Sleep"))&&(step>=7000)&&(gender.equals("Male"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Keep it up!!!"+"\n"+"Healthy diet");
        }
        else if((age>=26 && age<=64)&&(result.equals("Proper Sleep"))&&(step>=7000)&&(gender.equals("Male"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Keep it up!!!"+"\n"+"Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
//improper, male
        else if((age>=26 && age<=64)&&(result.equals("Improper Sleep"))&&(step>=7000)&&(gender.equals("Male"))&&(bmi<20)){
            //good step count && bad sleep but poor bmi(malnourished
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Work on improving your diet. Plan your meals.");
        }
        else if((age>=26 && age<=64)&&(result.equals("Improper Sleep"))&&(step>=7000)&&(gender.equals("Male"))&&(bmi>20 && bmi<25)){
            //step count good, sleep bad, bmi good
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Everything looks good. Stress less!");
        }
        else if((age>=26 && age<=64)&&(result.equals("Improper Sleep"))&&(step>=7000)&&(gender.equals("Male"))&&(bmi>25)){
            //step count good, bmi overweight, sleep bad
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
        //female,proper

        else if((age>=26 && age<=64)&&(result.equals("Proper Sleep"))&&(step>=7000)&&(gender.equals("Female"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Keep it up!!!"+"\n"+"Work on improving your diet. Plan your meals.");
        }
        else if((age>=26 && age<=64)&&(result.equals("Proper Sleep"))&&(step>=7000)&&(gender.equals("Female"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Keep it up!!!"+"\n"+"Healthy diet.");
        }
        else   if((age>=26 && age<=64)&&(result.equals("Proper Sleep"))&&(step>=7000)&&(gender.equals("Female"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Keep it up!!!"+"\n"+"Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
//female, improper

        else  if((age>=26 && age<=64)&&(result.equals("Improper Sleep"))&&(step>=7000)&&(gender.equals("Female"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Work on improving your diet. Plan your meals.");
        }
        else if((age>=26 && age<=64)&&(result.equals("Improper Sleep"))&&(step>=7000)&&(gender.equals("Female"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Everything looks good. Stress less!");
        }
        else  if((age>=26 && age<=64)&&(result.equals("Improper Sleep"))&&(step>=7000)&&(gender.equals("Female"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }

        //bad step count, male
        else  if((age>=26 && age<=64)&&(result.equals("Proper Sleep"))&&(step<7000)&&(gender.equals("Male"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Get more active. Try to take a small walk every 30 minutes!!!Work on improving your diet. Plan your meals.");
        }
        else if((age>=26 && age<=64)&&(result.equals("Proper Sleep"))&&(step<7000)&&(gender.equals("Male"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Get more active.Try to take a small walk every 30 minutes!!!Healthy diet.");
        }
        else  if((age>=26 && age<=64)&&(result.equals("Proper Sleep"))&&(step<7000)&&(gender.equals("Male"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Get more active. Try to take a small walk every 30 minutes!!!Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
//improper, bad step
        else  if((age>=26 && age<=64)&&(result.equals("Improper Sleep"))&&(step<7000)&&(gender.equals("Male"))&&(bmi<20)){
            //good step count && bad sleep but poor bmi(malnourished
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Work on improving your diet. Plan your meals.");
        }
        else  if((age>=26 && age<=64)&&(result.equals("Improper Sleep"))&&(step<7000)&&(gender.equals("Male"))&&(bmi>20 && bmi<25)){
            //step count good, sleep bad, bmi good
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Healthy diet.");
        }
        else   if((age>=26 && age<=64)&&(result.equals("Improper Sleep"))&&(step<7000)&&(gender.equals("Male"))&&(bmi>25)){
            //step count good, bmi overweight, sleep bad
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
//female, bad step
        else   if((age>=26 && age<=64)&&(result.equals("Proper Sleep"))&&(step<7000)&&(gender.equals("Female"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Get more active. Try to take a small walk every 30 minutes!!!Work on improving your diet. Plan your meals.");
        }
        else if((age>=26 && age<=64)&&(result.equals("Proper Sleep"))&&(step<7000)&&(gender.equals("Female"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Get more active. Try to take a small walk every 30 minutes!!!Healthy diet.");
        }
        else if((age>=26 && age<=64)&&(result.equals("Proper Sleep"))&&(step<7000)&&(gender.equals("Female"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Get more active. Try to take a small walk every 30 minutes!!!Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }

//improper, female, bad step
        else if((age>=26 && age<=64)&&(result.equals("Improper Sleep"))&&(step<7000)&&(gender.equals("Female"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Work on improving your diet. Plan your meals.");
        }
        else if((age>=26 && age<=64)&&(result.equals("Improper Sleep"))&&(step<7000)&&(gender.equals("Female"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Healthy diet.");
        }
        else if((age>=26 && age<=64)&&(result.equals("Improper Sleep"))&&(step<7000)&&(gender.equals("Female"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }


        //age>65

        else if((age >=65)&&(result.equals("Proper Sleep"))&&(step>=8000)&&(gender.equals("Male"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Keep it up!!!"+"\n"+"Work on improving your diet. Plan your meals.");
        }
        else if((age >=65)&&(result.equals("Proper Sleep"))&&(step>=8000)&&(gender.equals("Male"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Keep it up!!!"+"\n"+"Healthy diet.");
        }
        else  if((age >=65)&&(result.equals("Proper Sleep"))&&(step>=8000)&&(gender.equals("Male"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Keep it up!!!"+"\n"+"Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
//improper, male
        else if((age >=65)&&(result.equals("Improper Sleep"))&&(step>=8000)&&(gender.equals("Male"))&&(bmi<20)){
            //good step count && bad sleep but poor bmi(malnourished
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Work on improving your diet. Plan your meals.");
        }
        else if((age >=65)&&(result.equals("Improper Sleep"))&&(step>=8000)&&(gender.equals("Male"))&&(bmi>20 && bmi<25)){
            //step count good, sleep bad, bmi good
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Everything looks good. Stress less!");
        }
        else if((age >=65)&&(result.equals("Improper Sleep"))&&(step>=8000)&&(gender.equals("Male"))&&(bmi>25)){
            //step count good, bmi overweight, sleep bad
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
        //female,proper

        else if((age >=65)&&(result.equals("Proper Sleep"))&&(step>=8000)&&(gender.equals("Female"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Keep it up!!!"+"\n"+"Work on improving your diet. Plan your meals.");
        }
        else if((age >=65)&&(result.equals("Proper Sleep"))&&(step>=8000)&&(gender.equals("Female"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Keep it up!!!"+"\n"+"Healthy diet.");
        }
        else  if((age >=65)&&(result.equals("Proper Sleep"))&&(step>=8000)&&(gender.equals("Female"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Keep it up!!!"+"\n"+"Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
//female, improper

        else if((age >=65)&&(result.equals("Improper Sleep"))&&(step>=8000)&&(gender.equals("Female"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Work on improving your diet. Plan your meals.");
        }
        else if((age >=65)&&(result.equals("Improper Sleep"))&&(step>=8000)&&(gender.equals("Female"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Everything looks good. Stress less!");
        }
        else if((age >=65)&&(result.equals("Improper Sleep"))&&(step>=8000)&&(gender.equals("Female"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }

        //bad step count, male
        else  if((age >=65)&&(result.equals("Proper Sleep"))&&(step<8000)&&(gender.equals("Male"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Get active. Try to take a small walk every 30 minutes!!!Work on improving your diet. Plan your meals.");
        }
        else  if((age >=65)&&(result.equals("Proper Sleep"))&&(step<8000)&&(gender.equals("Male"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Get active. Try to take a small walk every 30 minutes!!!Healthy diet.");
        }
        else if((age >=65)&&(result.equals("Proper Sleep"))&&(step<8000)&&(gender.equals("Male"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Get active. Try to take a small walk every 30 minutes!!!Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
//improper, bad step
        else  if((age >=65)&&(result.equals("Improper Sleep"))&&(step<8000)&&(gender.equals("Male"))&&(bmi<20)){
            //good step count && bad sleep but poor bmi(malnourished
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active.Try to take a small walk every 30 minutes!!!Work on improving your diet. Plan your meals.");
        }
        else if((age >=65)&&(result.equals("Improper Sleep"))&&(step<8000)&&(gender.equals("Male"))&&(bmi>20 && bmi<25)){
            //step count good, sleep bad, bmi good
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Healthy diet.");
        }
        else if((age >=65)&&(result.equals("Improper Sleep"))&&(step<8000)&&(gender.equals("Male"))&&(bmi>25)){
            //step count good, bmi overweight, sleep bad
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }
//female, bad step
        else if((age >=65)&&(result.equals("Proper Sleep"))&&(step<8000)&&(gender.equals("Female"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Get active. Try to take a small walk every 30 minutes!!!Work on improving your diet. Plan your meals.");
        }
        else if((age >=65)&&(result.equals("Proper Sleep"))&&(step<8000)&&(gender.equals("Female"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Get active. Try to take a small walk every 30 minutes!!!Healthy diet.");
        }
        else  if((age >=65)&&(result.equals("Proper Sleep"))&&(step<8000)&&(gender.equals("Female"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Get active.Try to take a small walk every 30 minutes!!!Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }

//improper, female, bad step
        else if((age >=65)&&(result.equals("Improper Sleep"))&&(step<8000)&&(gender.equals("Female"))&&(bmi<20)){
            //good step count && good sleep but poor bmi(malnourished
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Work on improving your diet. Plan your meals.");
        }
        else if((age >=65)&&(result.equals("Improper Sleep"))&&(step<8000)&&(gender.equals("Female"))&&(bmi>20 && bmi<25)){
            //step count good, sleep good, bmi good
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Healthy diet.");
        }
        else if((age >=65)&&(result.equals("Improper Sleep"))&&(step<8000)&&(gender.equals("Female"))&&(bmi>25)){
            //step count good, bmi overweight, sleep good
            sug.setText("Improper Sleep!!!Try to meditate and do some yoga. Work on keeping your mind relaxed.Get more active. Try to take a small walk every 30 minutes!!!Work on your diet. You don't have to eat less, you just have to eat right. Plan your meals accordingly.");
        }

        else {
            sug.setText("Insufficient information available. Please complete your user profile");
        }


    }
}
