package amazenite.lockit;

import java.io.File;

import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

	/*
	 * Copyright (C) 2010 Daniel Nilsson
	 *
	 * Licensed under the Apache License, Version 2.0 (the "License");
	 * you may not use this file except in compliance with the License.
	 * You may obtain a copy of the License at
	 *
	 *      http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS,
	 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 * See the License for the specific language governing permissions and
	 * limitations under the License.
	 */

	public class ColorSelection extends PreferenceActivity implements
	                Preference.OnPreferenceClickListener {

        private final static int ACTIVITY_COLOR_PICKER_REQUEST_CODE = 1000;

        private Preference mDialogPreference;
        private Preference mActivityPreference;
        private Preference mGetSourceCodePreference;
        private int chosenColor = 0xff33CCCC;  //default color

	        @SuppressWarnings("deprecation")
			@Override
	        public void onCreate(Bundle savedInstanceState)
	        {
	                super.onCreate(savedInstanceState);
	                getWindow().setFormat(PixelFormat.RGBA_8888);

	                addPreferencesFromResource(R.xml.color_selection);
	        		File file = getBaseContext().getFileStreamPath("pickedColor");
	        	    if (!(file.exists())) //if it doesn't exist
	        	    {
	                saveColor(chosenColor);
	        	    }
	                final SharedPreferences prefs = PreferenceManager .getDefaultSharedPreferences(ColorSelection.this);
	                final ColorPickerDialog d = new ColorPickerDialog(this, prefs.getInt("dialog", 0xffffffff));
	                d.setAlphaSliderVisible(true);

                    d.setButton("Ok", new DialogInterface.OnClickListener()
                    {

	                    @Override
	                    public void onClick(DialogInterface dialog, int which) 
	                    {
	                            SharedPreferences.Editor editor = prefs.edit();
	                            editor.putInt("dialog", d.getColor());
	                            chosenColor = d.getColor();
	                            saveColor(chosenColor);
	                            editor.commit();	
	                            finish();
	                    }
                    });

                    d.setButton2("Cancel", new DialogInterface.OnClickListener() {
	
	                    @Override
	                    public void onClick(DialogInterface dialog, int which)
	                    {
	                    		finish();
	                    }
                    });

                    d.show();

	        }

	        @SuppressWarnings("deprecation")
			@Override
	        public boolean onPreferenceClick(Preference preference) {

	                final SharedPreferences prefs = PreferenceManager
	                                .getDefaultSharedPreferences(ColorSelection.this);
	                String key = preference.getKey();

	                if (key.equals("dialog")) {

	                        final ColorPickerDialog d = new ColorPickerDialog(this, prefs
	                                        .getInt("dialog", 0xffffffff));
	                        d.setAlphaSliderVisible(true);

	                        d.setButton("Ok", new DialogInterface.OnClickListener() {

	                                @Override
	                                public void onClick(DialogInterface dialog, int which) {

	                                        SharedPreferences.Editor editor = prefs.edit();
	                                        editor.putInt("dialog", d.getColor());
	                                        editor.commit();

	                                }
	                        });

	                        d.setButton2("Cancel", new DialogInterface.OnClickListener() {

	                                @Override
	                                public void onClick(DialogInterface dialog, int which) {

	                                }
	                        });

	                        d.show();

	                        return true;
	                } 

	                return false;
	        }

	        @Override
	        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	                if (requestCode == ACTIVITY_COLOR_PICKER_REQUEST_CODE
	                                && resultCode == Activity.RESULT_OK) {

	                        SharedPreferences customSharedPreference = PreferenceManager
	                                        .getDefaultSharedPreferences(ColorSelection.this);
	                        SharedPreferences.Editor editor = customSharedPreference.edit();
	                        editor.putInt("activity", data.getIntExtra(
	                                        ColorPickerActivity.RESULT_COLOR, 0xff000000));
	                        editor.commit();
	                }
	        }
	        
	        public void saveColor(int color)
	        {
	        	Constants.gestureColor = color;
	        }
	}