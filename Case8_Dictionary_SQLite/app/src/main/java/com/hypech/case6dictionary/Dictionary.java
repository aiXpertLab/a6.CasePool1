package com.hypech.case6dictionary;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class Dictionary extends Activity implements OnClickListener, TextWatcher
{
	// database path
	private final String DATABASE_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/dictionary";
	// input textdatabase
	private AutoCompleteTextView word;
	// database name
	private final String DATABASE_FILENAME = "dictionary.db";
	private SQLiteDatabase database;
	//search button
	private Button searchWord;
	//results
	private TextView showResult;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// Open database
		database = openDatabase();

		searchWord = (Button) findViewById(R.id.searchWord);
		word = (AutoCompleteTextView) findViewById(R.id.word);
		// Listener
		searchWord.setOnClickListener(this);
		word.addTextChangedListener(this);
		showResult=(TextView)findViewById(R.id.result);
	}

	// define DictionaryAdapter
	public class DictionaryAdapter extends CursorAdapter
	{
		private LayoutInflater layoutInflater;
		@Override
		public CharSequence convertToString(Cursor cursor)
		{
			return cursor == null ? "" : cursor.getString(cursor
					.getColumnIndex("_id"));
		}
		// put words into list view
		private void setView(View view, Cursor cursor)
		{
			TextView tvWordItem = (TextView) view;
			tvWordItem.setText(cursor.getString(cursor.getColumnIndex("_id")));
		}
		// bind selection to list
		@Override
		public void bindView(View view, Context context, Cursor cursor)
		{
			setView(view, cursor);
		}
		// create new view
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent)
		{
			View view = layoutInflater.inflate(R.layout.word_list_item, null);
			setView(view, cursor);
			return view;
		}
		public DictionaryAdapter(Context context, Cursor c, boolean autoRequery)
		{
			super(context, c, autoRequery);
			layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
	}
	public void afterTextChanged(Editable s)
	{
		//  English needs add _Id
		Cursor cursor = database.rawQuery(
				"select english as _id from t_words where english like ?",
				new String[]
						{ s.toString() + "%" });
		// new adapter
		DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
				cursor, true);
		// bind
		word.setAdapter(dictionaryAdapter);
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
								  int after)
	{
		// TODO Auto-generated method stub

	}
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		// TODO Auto-generated method stub

	}
	public void onClick(View view)
	{
		//search the word entered
		String sql = "select chinese from t_words where english=?";
		Cursor cursor = database.rawQuery(sql, new String[]
				{word.getText().toString()});
		String result = "not find, try again";
		//  show chinese meaning
		if (cursor.getCount() > 0)
		{
			//  must use movetofirst
			cursor.moveToFirst();
			result = cursor.getString(cursor.getColumnIndex("chinese")).replace("&amp;", "&");
		}
		// show results in textview
		showResult.setText(word.getText()+"\n"+result.toString());
	}

	private SQLiteDatabase openDatabase()
	{
		try
		{
			// absolute path
			String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
			File dir = new File(DATABASE_PATH);
			// if not /sdcard/dictionary, create it
			if (!dir.exists())
				dir.mkdir();
			// if not dictionary.db under /sdcard/dictionary
			// copy from res/raw
			if (!(new File(databaseFilename)).exists())
			{
				// stream
				InputStream is = getResources().openRawResource(
						R.raw.dictionary);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[8192];
				int count = 0;
				// start to copy
				while ((count = is.read(buffer)) > 0)
				{
					fos.write(buffer, 0, count);
				}
				// close stream
				fos.close();
				is.close();
			}
			// open db
			SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
					databaseFilename, null);
			return database;
		}
		catch (Exception e)
		{
		}
		// if cannot open it, return null
		return null;
	}
}