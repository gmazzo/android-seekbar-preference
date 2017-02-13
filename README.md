# percent-seekbar-pref
A SeekBarPreference that stores its value in a percentual relation (0 to 1) on max and min values

## Import
On your `build.gradle` add:
```
    dependencies {
        compile 'com.github.gmazzo:percent-seekbar-pref:0.1'
    }
```

## Usage
In your `preferences.xml` add:
```xml
    <gs.preference.PercentSeekBarPreference
        android:key="someKey"
        android:title="Percent value"
        android:defaultValue="0.7"/>
```
Use it as a drop-in replacement for `android.support.v7.preference.SeekBarPreference`
