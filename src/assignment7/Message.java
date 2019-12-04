/* CHAT ROOM <MyClass.java>
 * EE422C Project 7 submission by
 * Replace <...> with your actual data.
 * Christopher Saenz
 * cgs2258
 * 16185
 * Alan Sun
 * as79972
 * 16180
 * Slip days used: 1
 * Fall 2019
 */

package assignment7;

public class Message {
    Profile sender;
    String msg;
    public Message(String in){
        sender = null;
        msg = in;
    }
    public Message(Profile x, String in){
        sender = x;
        msg = in;
    }
}
