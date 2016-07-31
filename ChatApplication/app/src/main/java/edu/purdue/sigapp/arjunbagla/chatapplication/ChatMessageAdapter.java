package edu.purdue.sigapp.arjunbagla.chatapplication;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/*
  If you remember that ArrayAdapter we created last week, this week we are
  literally creating our own one of those. We extend BaseAdapter because
  it is the BASE class for all list adapters.
  When we extend baseadapter we need to implement a few methods, which
  you'll see below.
*/

public class ChatMessageAdapter extends BaseAdapter {

    private Context context;
    //private String userName;
    private List<ChatMessage> messages;

    public ChatMessageAdapter(Context context, List<ChatMessage> messages) {
        this.context = context;
        //this.userName = userName;
        this.messages = messages;
    }

    /*
      getCount is a method we have to override. It needs to return
      the total number of list items in our list. This will always be
      the size of the message list.
    */
    public int getCount() {
        return messages.size();
    }

    /*
      getItem is an override. We need to return the item at a given
      index in the list.
    */
    public Object getItem(int i) {
        return messages.get(i);
    }

    /*
      getItemId is an override. There is a reason for this to
      exist, but it will work perfectly fine if you just return 0.
    */
    public long getItemId(int i) {
        return 0;
    }

    /*
      This is the meat of our adapter.
      This method is called when Android needs to inflate the view of a given
      list item in the list. So we need to do this.
    */
    public View getView(int i, View recycle, ViewGroup viewGroup) {

        /*
          The 'recycle' passed in is an optimization the android system does.
          Inflating views is kind of expensive.
          When you scroll a long list, you can imagine that some views scroll
          off the screen and are no longer visible
          These views are still in memory, so android allows us to reuse them
          with that recycle paramter. This speeds up list scrolling.
          Thus, we only want to call inflater.inflate if the recycle parameter
          passed in is null.
        */
        if (recycle == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            recycle = inflater.inflate(R.layout.chat_message, viewGroup, false);
        }

        TextView userName = (TextView) recycle.findViewById(R.id.chat_message_username);
        TextView messageBody = (TextView) recycle.findViewById(R.id.chat_message_body);

        userName.setText(this.messages.get(i).getSender());
        messageBody.setText(this.messages.get(i).getText());
        messageBody.setText(this.messages.get(i).getText());

        return recycle;
    }

}