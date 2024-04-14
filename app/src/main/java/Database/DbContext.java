package Database;

import android.content.Context;

public class DbContext
{
    private static DatabaseReminder instance;

    public static String databaseName = "Hey";

    public static int version = 2;


    public static DatabaseReminder getInstance (Context c)
    {
        if (instance == null)
        {
            instance = new DatabaseReminder(c,databaseName,null,version);
        }

        return instance;
    }


}
