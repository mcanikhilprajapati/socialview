package com.example.socialview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.hendraanggrian.widget.Hashtag;
import com.hendraanggrian.widget.HashtagAdapter;
import com.hendraanggrian.widget.Mention;
import com.hendraanggrian.widget.MentionAdapter;
import com.hendraanggrian.widget.SocialAdapter;
import com.hendraanggrian.widget.SocialAutoCompleteTextView;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

import static android.util.Log.d;

public class MainActivity2 extends AppCompatActivity {

    private Toolbar toolbar;
    private SocialAutoCompleteTextView textView;

    private ArrayAdapter<Hashtag> defaultHashtagAdapter;
    private ArrayAdapter<Mention> defaultMentionAdapter;
    private ArrayAdapter<Person> customHashtagAdapter;
    private ArrayAdapter<Person> customMentionAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        textView = findViewById(R.id.textView);
        setSupportActionBar(toolbar);

        defaultHashtagAdapter = new HashtagAdapter(this);
        defaultHashtagAdapter.addAll(
                new Hashtag(getString(R.string.hashtag1)),
                new Hashtag(getString(R.string.hashtag2), getResources().getInteger(R.integer.hashtag2)),
                new Hashtag(getString(R.string.hashtag3), getResources().getInteger(R.integer.hashtag3)));

        defaultMentionAdapter = new MentionAdapter(this);
        defaultMentionAdapter.addAll(
                new Mention(getString(R.string.mention1_username)),
                new Mention(getString(R.string.mention2_username), getString(R.string.mention2_displayname), R.mipmap.ic_launcher),
                new Mention(getString(R.string.mention3_username), getString(R.string.mention3_displayname), "https://avatars0.githubusercontent.com/u/11507430?v=3&s=460"));

        customHashtagAdapter = new PersonAdapter(this);
        customHashtagAdapter.addAll(
                new Person(getString(R.string.hashtag1)),
                new Person(getString(R.string.hashtag2)),
                new Person(getString(R.string.hashtag3)));

        customMentionAdapter = new PersonAdapter(this);
        customMentionAdapter.addAll(
                new Person(getString(R.string.mention1_username)),
                new Person(getString(R.string.mention2_username)),
                new Person(getString(R.string.mention3_username)));

        textView.setThreshold(1);
        textView.setHashtagAdapter(defaultHashtagAdapter);
        textView.setMentionAdapter(defaultMentionAdapter);
        textView.setHashtagTextChangedListener(new Function2<MultiAutoCompleteTextView, String, Unit>() {
            @Override
            public Unit invoke(MultiAutoCompleteTextView multiAutoCompleteTextView, String s) {
                d("hashtag", s);
                return null;
            }
        });
        textView.setMentionTextChangedListener(new Function2<MultiAutoCompleteTextView, String, Unit>() {
            @Override
            public Unit invoke(MultiAutoCompleteTextView multiAutoCompleteTextView, String s) {
                d("mention", s);
                return null;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(!item.isChecked());
        switch (item.getItemId()) {
            case R.id.customAdapterItem:
                if (!item.isChecked()) {
                    textView.setHashtagAdapter(defaultHashtagAdapter);
                    textView.setMentionAdapter(defaultMentionAdapter);
                } else {
                    textView.setHashtagAdapter(customHashtagAdapter);
                    textView.setMentionAdapter(customMentionAdapter);
                }
                break;
            case R.id.enableHashtagItem:
                textView.setHashtagEnabled(item.isChecked());
                break;
            case R.id.enableMentionItem:
                textView.setMentionEnabled(item.isChecked());
                break;
            case R.id.enableHyperlinkItem:
                textView.setHyperlinkEnabled(item.isChecked());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    static class Person {
        final String name;

        Person(String name) {
            this.name = name;
        }
    }

    static class PersonAdapter extends SocialAdapter<Person> {

        PersonAdapter(Context context) {
            super(context, R.layout.item_person, R.id.textViewName);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_person, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Person item = getItem(position);
            if (item != null) holder.textView.setText(item.name);
            return convertView;
        }

        private static class ViewHolder {
            final TextView textView;

            ViewHolder(@NonNull View view) {
                this.textView = view.findViewById(R.id.textViewName);
            }
        }
    }
}