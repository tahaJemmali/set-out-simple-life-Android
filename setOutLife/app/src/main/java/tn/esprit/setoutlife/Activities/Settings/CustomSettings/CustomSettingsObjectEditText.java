package tn.esprit.setoutlife.Activities.Settings.CustomSettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hotmail.or_dvir.easysettings.enums.ESettingsTypes;
import com.hotmail.or_dvir.easysettings.pojos.SettingsObject;

import java.io.Serializable;

import tn.esprit.setoutlife.Activities.HomeActivity;
import tn.esprit.setoutlife.Activities.Settings.PersonalInfo.EmailActivity;
import tn.esprit.setoutlife.Activities.Settings.PersonalInfo.HomeTownActivity;
import tn.esprit.setoutlife.Activities.Settings.PersonalInfo.NameActivity;
import tn.esprit.setoutlife.Activities.Settings.PersonalInfo.PhoneActivity;
import tn.esprit.setoutlife.R;

@SuppressWarnings("PointlessBooleanExpression")
public class CustomSettingsObjectEditText extends SettingsObject<Void> implements Serializable
{
	private int number;
	public static String emailText;
	public static String passwordText;
	public static String firstNameText;
	public static String lastNameText;
	public static String phoneText;
	public static String homeTownText;

	public CustomSettingsObjectEditText(Builder builder)
	{
		super(builder.getKey(),
			  builder.getTitle(),
			  builder.getDefaultValue(),
			  builder.getSummary(),
              builder.getTextViewTitleId(),
              builder.getTextViewSummaryId(),
			  builder.getUseValueAsSummary(),
			  builder.hasDivider(),
			  builder.getType(),
			  builder.getImageViewIconId(),
			  builder.getIconDrawableId(),
			  builder.getIconDrawable());
	}

	public int incrementNumber()
	{
		return number++;
	}

	@Override
	public Void checkDataValidity(Context context, SharedPreferences prefs)
	{
		//in this case, there is no actual value saved,
		//so no validity check needed and we return null.
		return null;
	}

	@Override
	public int getLayout()
	{
		return R.layout.custom_settings_object_edit_text;
	}

	@Override
	public String getValueHumanReadable()
	{
		//in this case, there is no actual value save,
		//so just return null
		return null;
	}

	public EditText editText;
	//todo VERY IMPORTANT!!!!
	//todo when extending SettingsObject, you MUST override this method and call
	//todo super.initializeViews(root)!
	@Override
	public void initializeViews(View root)
	{
		//todo VERY IMPORTANT!!!!
		//todo you MUST call the super method in order to initialize basic views
		//todo such as the title TextView
	    super.initializeViews(root);

	    //Button btn = root.findViewById(R.id.button);
		//final EditText et = root.findViewById(R.id.editText);

		 editText = root.findViewById(R.id.editText);

		switch (getKey()){
			case EmailActivity.SETTINGS_KEY_EMAIL:
				editText.setHint("Enter new email address");
				editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
				break;
			case EmailActivity.SETTINGS_KEY_PASSWORD:
				editText.setHint("Enter Password");
				editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				editText.setTypeface(Typeface.DEFAULT);
				break;
			case NameActivity.SETTINGS_KEY_FIRSTNAME:
				editText.setHint("Enter your first name");
				editText.setText(HomeActivity.getCurrentLoggedInUser().getFirstName());
				editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
				firstNameText=editText.getText().toString();
				break;
			case NameActivity.SETTINGS_KEY_LASTNAME:
				editText.setHint("Enter your last name");
				editText.setText(HomeActivity.getCurrentLoggedInUser().getLastName());
				editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
				lastNameText=editText.getText().toString();
				break;
			case PhoneActivity.SETTINGS_KEY_PHONE:
				editText.setHint("Enter your phone number");
				if (!HomeActivity.getCurrentLoggedInUser().getPhone().equals("Not mentioned"))
				editText.setText(HomeActivity.getCurrentLoggedInUser().getPhone());
				editText.setInputType(InputType.TYPE_CLASS_NUMBER);
				phoneText=editText.getText().toString();
				break;
			case HomeTownActivity.SETTINGS_KEY_HOMETOWN:
				editText.setHint("Enter your home town");
				if (!HomeActivity.getCurrentLoggedInUser().getAddress().equals("Not mentioned"))
					editText.setText(HomeActivity.getCurrentLoggedInUser().getAddress());
				editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
				homeTownText=editText.getText().toString();
				break;
		}

		editText.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{
				//todo do something
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{
				switch (getKey()){
					case EmailActivity.SETTINGS_KEY_EMAIL:
						emailText=editText.getText().toString();
						break;
					case EmailActivity.SETTINGS_KEY_PASSWORD:
						passwordText=editText.getText().toString();
						break;
						case NameActivity.SETTINGS_KEY_FIRSTNAME:
							firstNameText=editText.getText().toString();
						break;
						case NameActivity.SETTINGS_KEY_LASTNAME:
							lastNameText=editText.getText().toString();
						break;
					case PhoneActivity.SETTINGS_KEY_PHONE:
						phoneText=editText.getText().toString();
						break;
					case HomeTownActivity.SETTINGS_KEY_HOMETOWN:
						homeTownText=editText.getText().toString();
						break;
				}
			}

			@Override
			public void afterTextChanged(Editable editable)
			{
				//todo do something
			}
		});

		root.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
//				showDialog(view);

				//todo if you'd like, you can also send a custom event here to notify
				//todo the settings activity of the click.
				//todo NOTE: this specific line is only an example and is copy-pasted
				//todo from the class BasicSettingsObject.
				//todo you need to make your own custom event here
//                EventBus.getDefault().post(new BasicSettingsClickEvent(BasicSettingsObject.this));
				//System.out.println("9licked");
			}
		});


		/*btn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				//todo do whatever you want here
				et.setText(incrementNumber() +"");
			}
		});*/

		/*et.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{
				//todo do something
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{
				//todo do something
			}

			@Override
			public void afterTextChanged(Editable editable)
			{
				//todo do something
			}
		});*/
	}

	/////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////

	public static class Builder extends SettingsObject.Builder<Builder, Void>
	{

        public Builder(String key, String title)
        {
            super(key,
				  title,
				  null,
				  R.id.editText,
				  null,
				  ESettingsTypes.VOID,
				  null);

		}

		@Override
		public CustomSettingsObjectEditText build()
		{
			return new CustomSettingsObjectEditText(this);
		}

	}
}
