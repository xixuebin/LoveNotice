package com.xixb.lovenotice;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private static final int PICK_CONTACT = 1010;
    TextView mPhoneNum;
    TextView mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=getIntent();
        String phone_num= intent.getStringExtra("phone_num");
        mPhoneNum = (TextView)findViewById(R.id.phone_num);
        mPhoneNum.setText(phone_num);
        String username = intent.getStringExtra("username");
        mUsername = (TextView)findViewById(R.id.user_name);
        mUsername.setText(username);

        Button btnAddLover = (Button) findViewById(R.id.add_lover);
        btnAddLover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoverInfo();
            }
        });
    }


    private void getLoverInfo(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);//vnd.android.cursor.dir/contact
        startActivityForResult(intent, PICK_CONTACT);
    }


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    if (contactData != null) {
                        //取得电话本中开始一项的光标，必须先moveToNext()
                        ContentResolver cr = getContentResolver();
                        Cursor cursor = cr.query(contactData, null, null, null, null);
                        while (cursor.moveToNext()) {
                            //取得联系人的名字索引
                            int nameIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                            String username = cursor.getString(nameIndex);
                            TextView mLoverUserName = (TextView)findViewById(R.id.lover_user_name);
                            mLoverUserName.setText(username);

                            //取得联系人的ID索引值
                            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                            //查询该位联系人的电话号码，类似的可以查询email，photo
                            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                            + contactId, null, null);//第一个参数是确定查询电话号，第三个参数是查询具体某个人的过滤值
                            //一个人可能有几个号码,但是我们只取第一个
                            if(phone.moveToNext()) {
                                String strPhoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                TextView mLoverPhoneNum = (TextView)findViewById(R.id.lover_phone_num);
                                mLoverPhoneNum.setText(strPhoneNumber);
                            }
                            phone.close();
                        }
                        cursor.close();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
