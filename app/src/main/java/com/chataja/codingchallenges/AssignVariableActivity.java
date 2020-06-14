package com.chataja.codingchallenges;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AssignVariableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignvariable);

        Service service;

        Class<ServiceA> a = ServiceA.class;
        Class<ServiceB> b = ServiceB.class;

        try {
            service = a.newInstance();
            System.out.println(service.b());

            service = b.newInstance();
            System.out.println(service.b());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}

interface Service {
    String b();
}

class ServiceA implements Service{
    public String b(){return "Q";}
}

class ServiceB implements Service{
    public String b(){return "R";}
}


