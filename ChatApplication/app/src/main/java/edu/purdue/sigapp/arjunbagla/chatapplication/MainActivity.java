package edu.purdue.sigapp.arjunbagla.chatapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

/**
 Our class extends one thing and implements another.
 First, it extends Activity. Every class which IS an activity has to extend Activity.
 You can think of an activity like a single screen of an app. It gets WAY more complicated
 when you want to build more complicated UIs, but this is the easiest way to think
 about it for now. We want a screen on our app, so we need an activity.
 Remember that every Activity has to be defined in your AndroidManifest.xml file.
 At minimum you'd need something like <activity name=".MainActivity"/> in there.
 Next, we say that it implements OnClickListener.
 A lot of Android has elements of event-based programming. When something happens,
 it fires an event which alerts us that it happened, then we can do stuff.
 One of those events is OnClick, and we use an OnClickListener to listen for OnClick events
 and do stuff. Easy.
 There are multiple ways to code this. In the presentation, i used an anonymous class.
 This way is a bit easier; we say that our Activity IS ITSELF an OnClickListener, and is
 thus capable of listening for click events. This requires we IMPLEMENT/OVERRIDE the
 method onClick(), which you can see we did below.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    /**
     We store references to our UI elements as private class variables.
     This is necessary because we need to be able to access at least the edittext
     in two different methods.
     */
    private BaseAdapter adapter;
    private ListView lv;
    private Button bu;
    private EditText et;

    /**
     Lets first talk about Adapters.
     Its easiest to think of Adapters as a BINDING between the UI and some source of data.
     There are multiple different ways to display a list of something on the screen (listview, gridview, etc...)
     There are also multiple different ways to source data (arrays, lists, etc...)
     Adapters store information about where your data you want to show IS AT and also what you want it to LOOK like.
     In essence: they ADAPT your data to fit on the display.
     */
    //private ArrayAdapter<String> adapter;

    /**
     Finally, we store a list of all the messages we want to display.
     At the start, this list will be empty.
     */
    private List<ChatMessage> messages = new ArrayList<ChatMessage>();

    private Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         Lets INFLATE our layout that we had defined in XML
         */
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        firebase = new Firebase("http://anvil-demo.firebaseio.com/messages");

        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                messages.add(dataSnapshot.getValue(ChatMessage.class));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        /**
         Now we get references to all the UI elements we defined earlier.
         findViewById is a method defined in the activity class. Thus because our
         class EXTENDS Activity, we can use it.
         findViewById returns a generic View object. Every UI element in Android
         extends View. Thus, we need to cast it to whatever view we know it
         actually is.
         Finally, what is R? Well, R is more magic. It stands for "Resource". Every time you
         edit any XML resource file, the android build tools auto-generate a class called R.
         This class is technically, almost magically, in the same package as this class
         file, so we don't have to import it. At runtime it contains in-memory references to UI elements,
         which we can reference with the id we assign them in the XML file.
         Thus, when you are working in XML, you reference other views by saying @id/my_id
         When working in Java, you reference them by saying R.id.my_id
         */
        lv = (ListView) findViewById(R.id.lv);
        bu = (Button) findViewById(R.id.send_button);
        et = (EditText) findViewById(R.id.message_box);

        /**
         Here we create our arrayadapter.
         `this` is just a reference to our Activity. Its something you see A LOT in android.
         Anything which has anything to do with a UI will usually require a Context as one
         of its arguments. Our class MainActivity extends Activity, and Activity extends
         Context. Thus, our class IS a context and 'this' works.
         We also pass in a resource identifier to layout which describes what we want
         each list item to look like. We use a default layout which is defined in
         android's R file called simple_list_item_1. Its basically just a textview.
         Finally, we pass in our datasource. ArrayAdapters support a few different
         basic sources of data like arrays or lists. We use a list.
         */
        adapter = new ChatMessageAdapter(this, messages);

        /**
         This binds the adapter we just created to our listview.
         With this call, our listview will display the data in the list
         */
        lv.setAdapter(adapter);

        /**
         Finally, we tell our button that we want to listen for when it is clicked.
         We must provide it an OnClickListener like I described earlier.
         One option is to do something like:
         bu.setOnClickListener(new View.OnClickListener() {
         public void onClick(View view) {
         ....
         }
         })
         This would create a new ANONYMOUS OnClickListener class. Anonymous classes are a feature
         of java, and it just means the class has no name. But this is a little ugly. So
         instead we just pass in `this` again. Because our class implements OnClickListener and
         provides a definition for the onClick method, this will work.
         */
        bu.setOnClickListener(this);
    }

    /**
     One more method. onClick, which will be called every time the button 'bu' is clicked.
     This method is passed in the view which was clicked. Imagine a situation where you
     want a single OnClickListener to listen for clicks from multiple different buttons.
     You can totally do this by looking at the ID of the view that was clicked (view.getId())
     But we only have one button here.
     */
    public void onClick(View view) {
        /**
         First we get the text the user typed in the edit text, store it in a string, and
         add that string to the end of our messages list.
         */
        String text = et.getText().toString();
        //messages.add(text);
        ChatMessage message = new ChatMessage("Arjun Bagla", text);
        firebase.push().setValue(message);

        /**
         We have the string in our list of data, but the listview hasn't been told that
         we added a new string. It isn't told "automatically"; we need to clue it into
         the fact that it might want to update itself. This is done on the adapter; we
         tell it "hey, you might want to recheck your source of data because we think it changed".
         This will update the listview.
         */
        //adapter.notifyDataSetChanged();

        /**
         This is some boilerplate. Don't worry about the specifics of what it is doing, because
         its way beyond the purposes of this tutorial.
         Just know: It closes the on-screen keyboard. Neat!
         */
        // Close The keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        /**
         Finally, we clear out the edittext of its text so the user can type more without having to backspace.
         To be honest, I'm not totally sure if this line works. But I'm not sure why. And I'm too lazy
         to debug it. LOL.
         But that's what this is supposed to do.
         */
        //Clear out the edit text
        et.getText().clear();
    }

}