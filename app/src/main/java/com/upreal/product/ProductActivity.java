package com.upreal.product;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.upreal.R;
import com.upreal.home.NavigationBar;
import com.upreal.list.AdapterListHomeCustom;
import com.upreal.list.ListActivity;
import com.upreal.login.LoginActivity;
import com.upreal.utils.BlurImages;
import com.upreal.utils.CircleTransform;
import com.upreal.utils.ConnectionDetector;
import com.upreal.utils.FragmentCommentary;
import com.upreal.utils.History;
import com.upreal.utils.IPDefiner;
import com.upreal.utils.Lists;
import com.upreal.utils.LocationService;
import com.upreal.utils.Product;
import com.upreal.utils.Refresh;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.SoapGlobalManager;
import com.upreal.utils.SoapProductUtilManager;
import com.upreal.utils.SoapUserUtilManager;
import com.upreal.utils.database.DatabaseHelper;
import com.upreal.utils.database.DatabaseQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Elyo on 11/02/2015.
 */
public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    LayoutInflater layoutInflater;
    private ConnectionDetector cd;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imageBlurred;
    private ImageView imageProduct;
    private TabLayout tabLayout;
    private Context context;
    private Activity activity;

    private ViewPager mViewPager;
    private ProductNewViewPagerAdapter adapter;
    private SessionManagerUser sessionManagerUser;

    private Product prod;
    private String listLike;
    private Boolean isLiked = false;
    private CharSequence title;

    private Product mProduct;

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;
    private DatabaseQuery mDbQuery;

    private TextView prodName;
    private TextView prodBrand;
    private TextView prodShortDesc;
    private ImageView prodPicture;
    private ProgressBar progressBar;

    private android.app.AlertDialog.Builder builder;
    private View layout;


    private FloatingActionButton menu;
    private FloatingActionButton like;
    private FloatingActionButton comment;
    private AlertDialog dialog;
    private Spinner spinner;
    private int idType;
    private EditText text;
    private View dialogView;
    private AlertDialog.Builder builderCustom;
    private AlertDialog.Builder builderList;
    private ArrayList<Integer> checkedList = new ArrayList<>();

    private String[] lists;

    private  ArrayList<Lists> checkList = new ArrayList<>();

    private int status = 0;
    LocationService locationService;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        context = getApplicationContext();
        activity = this;
        cd = new ConnectionDetector(context);
        locationService = LocationService.getLocationManager(getApplicationContext());
        tabLayout = (TabLayout) findViewById(R.id.tabsproduct);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        imageBlurred = (ImageView) findViewById(R.id.imageproductblurred);
        imageProduct = (ImageView) findViewById(R.id.imageproduct);
        progressBar = (ProgressBar) findViewById(R.id.progressBarProduct);

        prod = getIntent().getExtras().getParcelable("prod");
        mProduct = prod;
        if (prod == null)
            return;
        collapsingToolbarLayout.setTitle(prod.getName());
        if (cd.isConnectedToInternet()) {
            Picasso.with(getApplicationContext()).load(new IPDefiner().getIP() + "Symfony/web/images/Product/" + prod.getPicture()).transform(new BlurImages(getApplicationContext(), 25)).into(imageBlurred);
            Picasso.with(getApplicationContext()).load(new IPDefiner().getIP() + "Symfony/web/images/Product/" + prod.getPicture()).transform(new CircleTransform()).into(imageProduct);
        } else
            Toast.makeText(context, getResources().getString(R.string.no_internet_connection) + getResources().getString(R.string.please_reload), Toast.LENGTH_SHORT).show();
        CharSequence Tab[] = {"Info.", "Prix", "Avis"};
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ProductNewViewPagerAdapter(getSupportFragmentManager(), Tab, 3, prod, this, locationService);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
        menu = (FloatingActionButton) findViewById(R.id.fab);
        menu.setOnClickListener(this);
        like = (FloatingActionButton) findViewById(R.id.like);
        like.setOnClickListener(this);
        comment = (FloatingActionButton) findViewById(R.id.comment);
        comment.setOnClickListener(this);

        builder = new android.app.AlertDialog.Builder(this);

        sessionManagerUser = new SessionManagerUser(context);
        new History.createHistory(context, 1, 2 , prod.getId()).execute();

        mDbHelper = new DatabaseHelper(context);
        mDbQuery = new DatabaseQuery(mDbHelper);

        new NavigationBar(this);
        new RetrieveRateStatus().execute();

        final String[] option = new String[]{"Ajouter à ses ventes", "Ajouter à une liste", "Partager", "Rafraichir", "Suggestion"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quel action voulez-vous faire ?");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // Ajouter à ses ventes
                                layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                dialogView = layoutInflater.inflate(R.layout.dialog_add_usersell, null);
                                final EditText price = (EditText) dialogView.findViewById(R.id.product_price);

                                builderCustom = new AlertDialog.Builder(ProductActivity.this);
                                builderCustom.setView(dialogView)
                                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                if (price.getText().toString() == null || price.getText().toString().equals(""))
                                                    new AddUserSell().execute(0.);
                                                else
                                                    new AddUserSell().execute(Double.parseDouble(price.getText().toString()));
                                            }
                                        })
                                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                builderCustom.create().show();

                                break;
                            case 1: // Add to list
                                new RetrieveList().execute();
//                                mDbHelper = new DatabaseHelper(getApplicationContext());
//                                mDbQuery = new DatabaseQuery(mDbHelper);
//                                mDatabase = mDbHelper.openDataBase();
//
//                                final String[][] listsElements = mDbQuery.QueryGetElements("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, "type=?", new String[]{"8"}, null, null, null);
//                                mDatabase.close();
//                                lists = new String[listsElements.length];
//
//                                for (int i = 0; i < listsElements.length; i++) {
//                                    lists[i] = listsElements[i][0];
//                                }
//                                Toast.makeText(ProductActivity.this, "ListLength:" + lists[0], Toast.LENGTH_SHORT).show();
//                                layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                                dialogView = layoutInflater.inflate(R.layout.dialog_addproduct_list, null);
//                                TextView addCustom = (TextView) dialogView.findViewById(R.id.addcustom_list);
//
//                                addCustom.setOnClickListener(new View.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(View v) {
//                                        builderCustom = new AlertDialog.Builder(v.getContext());
//                                        LayoutInflater layoutInflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                                        View view = layoutInflater.inflate(R.layout.dialog_addlist, null);
//                                        final EditText editList = (EditText) view.findViewById(R.id.namelist);
//                                        builderCustom.setCancelable(false).setTitle(R.string.add_list).setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
//
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//
//                                                if (editList.getText().length() <= 0) {
//                                                    dialog.cancel();
//                                                }
//                                                dialog.dismiss();
//                                            }
//                                        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                dialog.cancel();
//                                            }
//                                        });
//                                        builderCustom.setView(view).create().show();
//                                    }
//                                });
//
//                                builderList = new AlertDialog.Builder(ProductActivity.this);
//                                builderList.setTitle(getString(R.string.add_product_in_which_list))
//                                        .setMultiChoiceItems(lists, null, new DialogInterface.OnMultiChoiceClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                                                if (isChecked) {
//                                                    checkedList.add(which);
//                                                } else if (checkedList.contains(which)) {
//                                                    checkedList.remove(Integer.valueOf(which));
//                                                }
//                                                mDbHelper = new DatabaseHelper(dialogView.getContext());
//                                                mDbQuery = new DatabaseQuery(mDbHelper);
//                                                mDatabase = mDbHelper.openDataBase();
//                                            }
//                                        })
//                                        .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                dialog.dismiss();
//                                            }
//                                        }).setNegativeButton(getString(R.string.cancel),
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                dialog.cancel();
//                                            }
//                                        }
//                                )
//                                        .create().show();
                                break;
                            case 2: // Share
                                Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Intent.ACTION_SEND);

                                i.putExtra(Intent.EXTRA_TEXT, "Venez voir le produit : " + prod.getName() + " sur UpReal");
                                i.setType("text/plain");
                                try {
                                    startActivity(Intent.createChooser(i, "Partager ce produit avec ..."));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(context, context.getString(R.string.need_mail_app)
                                            , Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 3: // Refresh
                                if (cd.isConnectedToInternet()) {
                                    new Refresh(activity, 2, prod.getId()).execute();
                                } else
                                    Toast.makeText(context, getResources().getString(R.string.no_internet_connection) + " " + getResources().getString(R.string.retry_retrieve_connection), Toast.LENGTH_SHORT).show();
                                break;
                            case 4: // Suggestion
                                layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                dialogView = layoutInflater.inflate(R.layout.dialog_suggestion, null);
                                spinner = (Spinner) dialogView.findViewById(R.id.spinner);
                                text = (EditText) dialogView.findViewById(R.id.text);

                                String[] array = {"Suggérer", "Signaler"};

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>
                                        (activity, android.R.layout.simple_spinner_item, array);

                                dataAdapter.setDropDownViewResource
                                        (android.R.layout.simple_spinner_dropdown_item);

                                spinner.setAdapter(dataAdapter);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (parent.getItemAtPosition(position).equals("Suggérer"))
                                            idType = 1;
                                        else if (parent.getItemAtPosition(position).equals("Signaler"))
                                            idType = 2;
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        idType = 0;
                                    }
                                });

                                builderCustom = new AlertDialog.Builder(ProductActivity.this);
                                builderCustom.setView(dialogView)
                                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                new AddSuggestion().execute(text.getText().toString());
                                            }
                                        })
                                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                builderCustom.create().show();
                                break;
                            default:
                                break;
                        }
                    }
                }
        );
        dialog = builder.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                //Refresh Fiche produit
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (!sessionManagerUser.isLogged()) {
                    builder.setMessage(R.string.error)
                            .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                }
                else
                    dialog.show();
                break;
            case R.id.like:
                if (cd.isConnectedToInternet()) {
                    listLike = context.getString(R.string.liked_product);

                    switch (status) {
                        case 1:
                            new SendRateStatus().execute(2);
                            break ;
                        case 2:
                            new SendRateStatus().execute(3);
                            break ;
                        case 3:
                            new SendRateStatus().execute(1);
                            break ;
                        default:
                            new SendRateStatus().execute(2);
                            break ;
                    }

                    new RetrieveRateStatus().execute();
                } else
                    Toast.makeText(context, getResources().getString(R.string.no_internet_connection) + " " + getResources().getString(R.string.retry_retrieve_connection), Toast.LENGTH_SHORT).show();
                break ;
            case R.id.comment:
                if (cd.isConnectedToInternet()) {
                    if (!sessionManagerUser.isLogged()) {
                        builder.setTitle("Vous voulez commenter cet utilisateur ?").setMessage("Connectez vous pour partager votre opinion")
                                .setPositiveButton(v.getContext().getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        context.startActivity(intent);
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton(v.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create().show();
                    } else {
                        builder.setTitle("Votre commentaire");
                        LayoutInflater inflater = LayoutInflater.from(context);
                        layout = inflater.inflate(R.layout.dialog_comment, null);
                        builder.setView(layout);
                        final EditText comment = (EditText) layout.findViewById(R.id.comment);
                        final TextView limit = (TextView) layout.findViewById(R.id.limit);
                        comment.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                limit.setText(s.length() + " / " + String.valueOf(250));
                                if (s.length() > 250)
                                    comment.setText(s.subSequence(0, 250));
                            }
                        });
                        builder.setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (comment.getText().toString().equals(""))
                                    Toast.makeText(context, "Le commentaire ne peut etre vide", Toast.LENGTH_SHORT).show();
                                else
                                    new FragmentCommentary.SendComment(comment.getText().toString(), context, sessionManagerUser.getUserId(), prod.getId(), 2).execute();
                            }
                        });
                        builder.setNegativeButton(v.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.create().show();
                    }
                } else
                    Toast.makeText(context, getResources().getString(R.string.no_internet_connection) + getResources().getString(R.string.retry_retrieve_connection), Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private class RetrieveList extends AsyncTask<Void, Void, List<Lists>> {

        @Override
        protected List<Lists> doInBackground(Void... params) {


            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            if (userSession != null && userSession.isLogged()) {
                SoapGlobalManager gm = new SoapGlobalManager();
                List<Lists> lList = gm.getUserList(userSession.getUserId());

                return lList;
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<Lists> res) {
            super.onPostExecute(res);
            ArrayList<CharSequence> lists = new ArrayList<>();

            for (Lists item : res) {
                if (item.getId() > 5)
                    lists.add(item.getName());
            }
            Log.e("ProductActivity", "WebService called. Result:");
            if (lists.size() == 0) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("Dialog");
                builder.setMessage("Veuillez créer une liste.");
                builder.setPositiveButton("OK", null);
                builder.show();
                return;
            }
            final CharSequence[] dialogList=  lists.toArray(new CharSequence[lists.size()]);
            final AlertDialog.Builder builderDialog = new AlertDialog.Builder(ProductActivity.this);
            builderDialog.setTitle(R.string.add_list);
            int count = dialogList.length;
            boolean[] is_checked = new boolean[count];


            builderDialog.setMultiChoiceItems(dialogList, is_checked,
                    new DialogInterface.OnMultiChoiceClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton, boolean isChecked) {
                            if (isChecked) {
                                for (Lists item : res) {
                                    if (dialogList[whichButton] == item.getName())
                                        checkList.add(item);
                                }
                            } else if (!isChecked) {
                                for (Lists item : res) {
                                    if (dialogList[whichButton] == item.getName())
                                        checkList.remove(item);
                                }
                            }
                            Toast.makeText(getApplicationContext(),"Total List " + checkList.size(), Toast.LENGTH_SHORT).show();
                        }
                    });

            builderDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (checkList.size() > 0)
                                new CreateItemList().execute();
                        }
                    });

            builderDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog alert = builderDialog.create();
            alert.show();
        }
    }

    private class CreateItemList extends AsyncTask<Void, Void, Integer> {
        SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

        @Override
        protected Integer doInBackground(Void... voids) {
            SoapGlobalManager gm = new SoapGlobalManager();
            for (Lists item : checkList) {
                int res = gm.createItem(item.getId(), mProduct.getId(), userSession.getUserId());
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }
    }

    private class RetrieveRateStatus extends AsyncTask<Void, Void, Integer> {


        int likeV = 0;
        int dislikeV = 0;

        @Override
        protected Integer doInBackground(Void... params) {

            SoapGlobalManager gm = new SoapGlobalManager();
            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            likeV = gm.countRate(prod.getId(), 2, 2);
            dislikeV = gm.countRate(prod.getId(), 2, 3);
            if (userSession != null && userSession.isLogged()) {
                return gm.getRateStatus(prod.getId(), 2, userSession.getUserId());
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer res) {
            super.onPostExecute(res);

            status = res;
            switch (status) {
                case 1:
                    like.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.result_minor_text)));
                    break;
                case 2:
                    like.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    break;
                case 3:
                    like.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    break;
            }
            Log.e("TEST", "stat: " + res);
            new Thread(new Runnable() {
                public void run() {
                    progressBar.setMax(likeV + dislikeV);
                    while (progressStatus < likeV) {
                        progressStatus += 1;
                        // Update the progress bar and display the
                        //current value in the text view
                        handler.post(new Runnable() {
                            public void run() {
                                progressBar.setProgress(progressStatus);
                            }
                        });
                        try {
                            // Sleep for 200 milliseconds.
                            //Just to display the progress slowly
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    private class SendRateStatus extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            Log.e("ProductActivity", "SendRateStatus called :" + params[0]);

            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            if (userSession != null && userSession.isLogged()) {
                SoapGlobalManager gm = new SoapGlobalManager();

                Log.e("ProductActivity", "SENDING : I am user " + userSession.getUserId());
                switch (params[0]) {
                    case 1:
                        gm.unLikeSomething(prod.getId(), 2, userSession.getUserId());
                        break ;
                    case 2:
                        gm.likeSomething(prod.getId(), 2, userSession.getUserId());
                        break ;
                    case 3:
                        gm.dislikeSomething(prod.getId(), 2, userSession.getUserId());
                        break ;
                    default:
                        break ;
                }

                SoapUserUtilManager uum = new SoapUserUtilManager();

                switch (params[0]) {
                    case 1:
                        uum.createHistory(userSession.getUserId(), 4, 2, prod.getId());
                        break ;
                    case 2:
                        uum.createHistory(userSession.getUserId(), 2, 2, prod.getId());
                        break ;
                    case 3:
                        uum.createHistory(userSession.getUserId(), 3, 2, prod.getId());
                        break ;
                    default:
                        break ;
                }
            }

            return null;
        }
    }

    private class AddUserSell extends AsyncTask<Double, Void, Void> {
        @Override
        protected Void doInBackground(Double... params) {
            SoapProductUtilManager pum = new SoapProductUtilManager();

            if (sessionManagerUser.isLogged())
                pum.createUserSell(prod.getId(), sessionManagerUser.getUserId(), params[0]);

            return null;
        }
    }

    private class AddSuggestion extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            SoapGlobalManager gm = new SoapGlobalManager();

            if (sessionManagerUser.isLogged())
                gm.createSuggestion(sessionManagerUser.getUserId(), idType, 2, prod.getId(), params[0]);

            return null;
        }
    }
}