package fr.uha.ensisa.huynhphuc.supernewsbrowser;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.DaoMaster;
import fr.uha.ensisa.huynhphuc.supernewsbrowser.model.DaoSession;

public class App extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        // regular SQLite database
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "articles-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        //DaoMaster.dropAllTables(getDaoSession().getDatabase(), true);
        //DaoMaster.createAllTables(getDaoSession().getDatabase(), true);

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}