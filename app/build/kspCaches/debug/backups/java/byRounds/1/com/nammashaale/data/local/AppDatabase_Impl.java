package com.nammashaale.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile AssetDao _assetDao;

  private volatile AuditDao _auditDao;

  private volatile RepairDao _repairDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `assets` (`assetId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `assetName` TEXT NOT NULL, `category` TEXT NOT NULL, `serialNumber` TEXT NOT NULL, `purchaseDate` INTEGER NOT NULL, `location` TEXT NOT NULL, `condition` TEXT NOT NULL, `imagePath` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `audits` (`auditId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `assetId` INTEGER NOT NULL, `status` TEXT NOT NULL, `checkedDate` INTEGER NOT NULL, `remarks` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `repairs` (`repairId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `assetId` INTEGER NOT NULL, `issue` TEXT NOT NULL, `status` TEXT NOT NULL, `requestDate` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '4e5d3f28c825c058e7907ab711835734')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `assets`");
        db.execSQL("DROP TABLE IF EXISTS `audits`");
        db.execSQL("DROP TABLE IF EXISTS `repairs`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsAssets = new HashMap<String, TableInfo.Column>(8);
        _columnsAssets.put("assetId", new TableInfo.Column("assetId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssets.put("assetName", new TableInfo.Column("assetName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssets.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssets.put("serialNumber", new TableInfo.Column("serialNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssets.put("purchaseDate", new TableInfo.Column("purchaseDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssets.put("location", new TableInfo.Column("location", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssets.put("condition", new TableInfo.Column("condition", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAssets.put("imagePath", new TableInfo.Column("imagePath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAssets = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAssets = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAssets = new TableInfo("assets", _columnsAssets, _foreignKeysAssets, _indicesAssets);
        final TableInfo _existingAssets = TableInfo.read(db, "assets");
        if (!_infoAssets.equals(_existingAssets)) {
          return new RoomOpenHelper.ValidationResult(false, "assets(com.nammashaale.data.local.AssetEntity).\n"
                  + " Expected:\n" + _infoAssets + "\n"
                  + " Found:\n" + _existingAssets);
        }
        final HashMap<String, TableInfo.Column> _columnsAudits = new HashMap<String, TableInfo.Column>(5);
        _columnsAudits.put("auditId", new TableInfo.Column("auditId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudits.put("assetId", new TableInfo.Column("assetId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudits.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudits.put("checkedDate", new TableInfo.Column("checkedDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudits.put("remarks", new TableInfo.Column("remarks", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAudits = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAudits = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAudits = new TableInfo("audits", _columnsAudits, _foreignKeysAudits, _indicesAudits);
        final TableInfo _existingAudits = TableInfo.read(db, "audits");
        if (!_infoAudits.equals(_existingAudits)) {
          return new RoomOpenHelper.ValidationResult(false, "audits(com.nammashaale.data.local.AuditEntity).\n"
                  + " Expected:\n" + _infoAudits + "\n"
                  + " Found:\n" + _existingAudits);
        }
        final HashMap<String, TableInfo.Column> _columnsRepairs = new HashMap<String, TableInfo.Column>(5);
        _columnsRepairs.put("repairId", new TableInfo.Column("repairId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepairs.put("assetId", new TableInfo.Column("assetId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepairs.put("issue", new TableInfo.Column("issue", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepairs.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRepairs.put("requestDate", new TableInfo.Column("requestDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRepairs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRepairs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRepairs = new TableInfo("repairs", _columnsRepairs, _foreignKeysRepairs, _indicesRepairs);
        final TableInfo _existingRepairs = TableInfo.read(db, "repairs");
        if (!_infoRepairs.equals(_existingRepairs)) {
          return new RoomOpenHelper.ValidationResult(false, "repairs(com.nammashaale.data.local.RepairEntity).\n"
                  + " Expected:\n" + _infoRepairs + "\n"
                  + " Found:\n" + _existingRepairs);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "4e5d3f28c825c058e7907ab711835734", "33229b360f650afa6892e31365dc40e1");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "assets","audits","repairs");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `assets`");
      _db.execSQL("DELETE FROM `audits`");
      _db.execSQL("DELETE FROM `repairs`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AssetDao.class, AssetDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AuditDao.class, AuditDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RepairDao.class, RepairDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public AssetDao assetDao() {
    if (_assetDao != null) {
      return _assetDao;
    } else {
      synchronized(this) {
        if(_assetDao == null) {
          _assetDao = new AssetDao_Impl(this);
        }
        return _assetDao;
      }
    }
  }

  @Override
  public AuditDao auditDao() {
    if (_auditDao != null) {
      return _auditDao;
    } else {
      synchronized(this) {
        if(_auditDao == null) {
          _auditDao = new AuditDao_Impl(this);
        }
        return _auditDao;
      }
    }
  }

  @Override
  public RepairDao repairDao() {
    if (_repairDao != null) {
      return _repairDao;
    } else {
      synchronized(this) {
        if(_repairDao == null) {
          _repairDao = new RepairDao_Impl(this);
        }
        return _repairDao;
      }
    }
  }
}
