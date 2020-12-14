package tn.esprit.setoutlife.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import tn.esprit.setoutlife.Activities.Profile.ProfileActivity;
import tn.esprit.setoutlife.Fragments.ForumFragment;
import tn.esprit.setoutlife.Fragments.HomeFragment;
import tn.esprit.setoutlife.Fragments.ProfilFragment;
import tn.esprit.setoutlife.Fragments.SettingsFragment;
import tn.esprit.setoutlife.Fragments.TaskFragment;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Utils.CallBackInterface;
import tn.esprit.setoutlife.entities.User;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CallBackInterface {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    CircleImageView profilImageInNavBar;
    TextView tvNameInNavBar;

    View header;

    private static User CurrentLoggedInUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    /************************ fragment manager ********************************/
        fragmentManager = getSupportFragmentManager();

    /************************ Hooks ********************************/
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        /************************ tool bar ********************************/
        setSupportActionBar(toolbar);
        //getSupportActionBar().hide();
        toolbar.getBackground().setAlpha(0);
        getSupportActionBar().setTitle("Welcome "+"Fahd !");

        /************************ navigation drawer menu ********************************/
        navigationView.bringToFront();
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Default fragment
        if (savedInstanceState == null){ //rotation thing solver
            addHomeFragment(null);
        }
        navigationView.setNavigationItemSelectedListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);



        /************************** header elements **************************/
        header = navigationView.getHeaderView(0);
        tvNameInNavBar = header.findViewById(R.id.tvNameInNavBar);
        tvNameInNavBar.setText(getCurrentLoggedInUser().getFirstName()+" "+getCurrentLoggedInUser().getLastName());
        profilImageInNavBar = header.findViewById(R.id.profilImageInNavBar);

        /*if (!HomeActivity.getCurrentLoggedInUser().getPhoto().equals("Not mentioned"))
            Picasso.get().load(HomeActivity.getCurrentLoggedInUser().getPhoto()).into(profilImageInNavBar);
        else
            Picasso.get().load("https://graph.facebook.com/10214899562601635/picture?height=1024").into(profilImageInNavBar);
*/


        if (HomeActivity.getCurrentLoggedInUser().getPhoto().startsWith("/")) {
            Bitmap bitmap = getBitmapFromString(HomeActivity.getCurrentLoggedInUser().getPhoto());
            profilImageInNavBar.setImageBitmap(bitmap);
        }
        else if (!HomeActivity.getCurrentLoggedInUser().getPhoto().equals("Not mentioned")){
            Picasso.get().load(HomeActivity.getCurrentLoggedInUser().getPhoto()).into(profilImageInNavBar);
        }
        else {
            Picasso.get().load("https://graph.facebook.com/10214899562601635/picture?height=1024").into(profilImageInNavBar);
        }

       // System.out.println(HomeActivity.getCurrentLoggedInUser().getPhoto());
    }



    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    protected void onResume() {
        //toggleFullscreen(false);
        //System.out.println(fragmentManager.getBackStackEntryCount());
        tvNameInNavBar.setText(getCurrentLoggedInUser().getFirstName()+" "+getCurrentLoggedInUser().getLastName());
        if (fragmentManager.getBackStackEntryCount()==0){ //when bach pressed from settings activity
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                fragmentManager.popBackStack();
                addHomeFragment(null);
                toggleFullscreen(false);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                break;
            case R.id.nav_Profil:
                //addProfilFragment(null,true);
                openProfileActivity();
                break;
            case R.id.nav_Schedule:
                return false;
                //break;
            case R.id.nav_Tasks:
                addTaskFragment(null,true);
                break;
            case R.id.nav_Finance:
                return false;
            //break;
            //case R.id.nav_ProgressAndStatistics:
                //return false;
            //break;
            case R.id.nav_Forum:
                addForumFragment(null,true);
                break;
            case R.id.nav_settings:
                //addSettingsFragment(null,true);
                openSettingsActivity();
            break;
            //case R.id.nav_notification:
                //return false;
            //break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void NavButtonSelected() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void openSettingsActivity(){
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openProfileActivity(){
        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void addHomeFragment(Bundle bundle) {
        fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setCallBackInterface(this);
        fragmentTransaction.replace(R.id.fragment_container,homeFragment);
        fragmentTransaction.commit();
    }

    private void addProfilFragment(Bundle bundle,Boolean fromNavBar) {
        fragmentTransaction = fragmentManager.beginTransaction();
        ProfilFragment profilFragment = new ProfilFragment();
        profilFragment.setCallBackInterface(this);
        fragmentTransaction.replace(R.id.fragment_container,profilFragment);
        if (!fromNavBar) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addForumFragment(Bundle bundle,Boolean fromNavBar) {
        fragmentTransaction = fragmentManager.beginTransaction();
        ForumFragment forumFragment = new ForumFragment();
        forumFragment.setCallBackInterface(this);
        fragmentTransaction.replace(R.id.fragment_container,forumFragment);
        if (!fromNavBar) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addSettingsFragment(Bundle bundle,Boolean fromNavBar) {
        fragmentTransaction = fragmentManager.beginTransaction();
        SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.setCallBackInterface(this);
        fragmentTransaction.replace(R.id.fragment_container,settingsFragment);
        if (!fromNavBar) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addTaskFragment(Bundle bundle,Boolean fromNavBar) {
        fragmentTransaction = fragmentManager.beginTransaction();
        TaskFragment taskFragment = new TaskFragment();
        taskFragment.setCallBackInterface(this);
        fragmentTransaction.replace(R.id.fragment_container,taskFragment);
        if (!fromNavBar) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            super.onBackPressed();
            Toast.makeText(this, "OnBAckPressed Works", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }*/



    @Override
    public void popBack() {
        fragmentManager.popBackStack();
        addHomeFragment(null);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        System.out.println("pop back interface ceolled in homeActivity");
        toggleFullscreen(false);

    }

    @Override
    public void openFragment(String name) {
        switch (name){
            case "Profil":
                openProfileActivity();
                //toggleFullscreen(true);
                navigationView.setCheckedItem(R.id.nav_Profil);
                /*addProfilFragment(null,false);
                toggleFullscreen(true);
                navigationView.setCheckedItem(R.id.nav_Profil);*/
                break;
            case "Schedule":// TODO addScheduleFragment(null);
                break;
            case "Tasks":addTaskFragment(null,false);
                toggleFullscreen(true);
                navigationView.setCheckedItem(R.id.nav_Tasks);
                break;
            case "Finance"://TODO addFinanceFragment(null);
                break;
            case "Progress & Statistics"://TODO addProgressStatFragment(null);
                break;
            case "Forum":addForumFragment(null,false);
                toggleFullscreen(true);
                navigationView.setCheckedItem(R.id.nav_Forum);
                break;
            case "Settings"://addSettingsFragment(null,false);
                openSettingsActivity();
                //toggleFullscreen(true);
                navigationView.setCheckedItem(R.id.nav_settings);
                break;
            case "Tutorial"://TODO addTutorialFragment(null);
                break;
            case "Notification"://TODO addForumFragment(null);
                break;
            case "Support"://TODO addSupportFragment(null);
                break;

        }
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void toggleFullscreen(boolean fullscreen)
    {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        if (fullscreen)
        {
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        else
        {
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        getWindow().setAttributes(attrs);
    }

    public static User getCurrentLoggedInUser(){
        return CurrentLoggedInUser;
    }

    public static void setCurrentLoggedInUser (User user){
        CurrentLoggedInUser=user;
    }

}