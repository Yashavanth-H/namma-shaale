package com.nammashaale.data.local;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AssetDao_Impl implements AssetDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AssetEntity> __insertionAdapterOfAssetEntity;

  private final EntityDeletionOrUpdateAdapter<AssetEntity> __deletionAdapterOfAssetEntity;

  private final EntityDeletionOrUpdateAdapter<AssetEntity> __updateAdapterOfAssetEntity;

  public AssetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAssetEntity = new EntityInsertionAdapter<AssetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `assets` (`assetId`,`assetName`,`category`,`serialNumber`,`purchaseDate`,`location`,`condition`,`imagePath`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AssetEntity entity) {
        statement.bindLong(1, entity.getAssetId());
        statement.bindString(2, entity.getAssetName());
        statement.bindString(3, entity.getCategory());
        statement.bindString(4, entity.getSerialNumber());
        statement.bindLong(5, entity.getPurchaseDate());
        statement.bindString(6, entity.getLocation());
        statement.bindString(7, entity.getCondition());
        if (entity.getImagePath() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getImagePath());
        }
      }
    };
    this.__deletionAdapterOfAssetEntity = new EntityDeletionOrUpdateAdapter<AssetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `assets` WHERE `assetId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AssetEntity entity) {
        statement.bindLong(1, entity.getAssetId());
      }
    };
    this.__updateAdapterOfAssetEntity = new EntityDeletionOrUpdateAdapter<AssetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `assets` SET `assetId` = ?,`assetName` = ?,`category` = ?,`serialNumber` = ?,`purchaseDate` = ?,`location` = ?,`condition` = ?,`imagePath` = ? WHERE `assetId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AssetEntity entity) {
        statement.bindLong(1, entity.getAssetId());
        statement.bindString(2, entity.getAssetName());
        statement.bindString(3, entity.getCategory());
        statement.bindString(4, entity.getSerialNumber());
        statement.bindLong(5, entity.getPurchaseDate());
        statement.bindString(6, entity.getLocation());
        statement.bindString(7, entity.getCondition());
        if (entity.getImagePath() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getImagePath());
        }
        statement.bindLong(9, entity.getAssetId());
      }
    };
  }

  @Override
  public Object insertAsset(final AssetEntity asset, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAssetEntity.insertAndReturnId(asset);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAsset(final AssetEntity asset, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfAssetEntity.handle(asset);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateAsset(final AssetEntity asset, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAssetEntity.handle(asset);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AssetEntity>> getAllAssets() {
    final String _sql = "SELECT * FROM assets ORDER BY assetName ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"assets"}, new Callable<List<AssetEntity>>() {
      @Override
      @NonNull
      public List<AssetEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfAssetId = CursorUtil.getColumnIndexOrThrow(_cursor, "assetId");
          final int _cursorIndexOfAssetName = CursorUtil.getColumnIndexOrThrow(_cursor, "assetName");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSerialNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "serialNumber");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfCondition = CursorUtil.getColumnIndexOrThrow(_cursor, "condition");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final List<AssetEntity> _result = new ArrayList<AssetEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AssetEntity _item;
            final int _tmpAssetId;
            _tmpAssetId = _cursor.getInt(_cursorIndexOfAssetId);
            final String _tmpAssetName;
            _tmpAssetName = _cursor.getString(_cursorIndexOfAssetName);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpSerialNumber;
            _tmpSerialNumber = _cursor.getString(_cursorIndexOfSerialNumber);
            final long _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getLong(_cursorIndexOfPurchaseDate);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final String _tmpCondition;
            _tmpCondition = _cursor.getString(_cursorIndexOfCondition);
            final String _tmpImagePath;
            if (_cursor.isNull(_cursorIndexOfImagePath)) {
              _tmpImagePath = null;
            } else {
              _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            }
            _item = new AssetEntity(_tmpAssetId,_tmpAssetName,_tmpCategory,_tmpSerialNumber,_tmpPurchaseDate,_tmpLocation,_tmpCondition,_tmpImagePath);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<AssetEntity> getAssetById(final int id) {
    final String _sql = "SELECT * FROM assets WHERE assetId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"assets"}, new Callable<AssetEntity>() {
      @Override
      @Nullable
      public AssetEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfAssetId = CursorUtil.getColumnIndexOrThrow(_cursor, "assetId");
          final int _cursorIndexOfAssetName = CursorUtil.getColumnIndexOrThrow(_cursor, "assetName");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSerialNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "serialNumber");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfCondition = CursorUtil.getColumnIndexOrThrow(_cursor, "condition");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final AssetEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpAssetId;
            _tmpAssetId = _cursor.getInt(_cursorIndexOfAssetId);
            final String _tmpAssetName;
            _tmpAssetName = _cursor.getString(_cursorIndexOfAssetName);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpSerialNumber;
            _tmpSerialNumber = _cursor.getString(_cursorIndexOfSerialNumber);
            final long _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getLong(_cursorIndexOfPurchaseDate);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final String _tmpCondition;
            _tmpCondition = _cursor.getString(_cursorIndexOfCondition);
            final String _tmpImagePath;
            if (_cursor.isNull(_cursorIndexOfImagePath)) {
              _tmpImagePath = null;
            } else {
              _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            }
            _result = new AssetEntity(_tmpAssetId,_tmpAssetName,_tmpCategory,_tmpSerialNumber,_tmpPurchaseDate,_tmpLocation,_tmpCondition,_tmpImagePath);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<AssetEntity>> getAssetsByCategory(final String category) {
    final String _sql = "SELECT * FROM assets WHERE category = ? ORDER BY assetName ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, category);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"assets"}, new Callable<List<AssetEntity>>() {
      @Override
      @NonNull
      public List<AssetEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfAssetId = CursorUtil.getColumnIndexOrThrow(_cursor, "assetId");
          final int _cursorIndexOfAssetName = CursorUtil.getColumnIndexOrThrow(_cursor, "assetName");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSerialNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "serialNumber");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfCondition = CursorUtil.getColumnIndexOrThrow(_cursor, "condition");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final List<AssetEntity> _result = new ArrayList<AssetEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AssetEntity _item;
            final int _tmpAssetId;
            _tmpAssetId = _cursor.getInt(_cursorIndexOfAssetId);
            final String _tmpAssetName;
            _tmpAssetName = _cursor.getString(_cursorIndexOfAssetName);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpSerialNumber;
            _tmpSerialNumber = _cursor.getString(_cursorIndexOfSerialNumber);
            final long _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getLong(_cursorIndexOfPurchaseDate);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final String _tmpCondition;
            _tmpCondition = _cursor.getString(_cursorIndexOfCondition);
            final String _tmpImagePath;
            if (_cursor.isNull(_cursorIndexOfImagePath)) {
              _tmpImagePath = null;
            } else {
              _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            }
            _item = new AssetEntity(_tmpAssetId,_tmpAssetName,_tmpCategory,_tmpSerialNumber,_tmpPurchaseDate,_tmpLocation,_tmpCondition,_tmpImagePath);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<AssetEntity>> getAssetsByCondition(final String condition) {
    final String _sql = "SELECT * FROM assets WHERE condition = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, condition);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"assets"}, new Callable<List<AssetEntity>>() {
      @Override
      @NonNull
      public List<AssetEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfAssetId = CursorUtil.getColumnIndexOrThrow(_cursor, "assetId");
          final int _cursorIndexOfAssetName = CursorUtil.getColumnIndexOrThrow(_cursor, "assetName");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSerialNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "serialNumber");
          final int _cursorIndexOfPurchaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "purchaseDate");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfCondition = CursorUtil.getColumnIndexOrThrow(_cursor, "condition");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final List<AssetEntity> _result = new ArrayList<AssetEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AssetEntity _item;
            final int _tmpAssetId;
            _tmpAssetId = _cursor.getInt(_cursorIndexOfAssetId);
            final String _tmpAssetName;
            _tmpAssetName = _cursor.getString(_cursorIndexOfAssetName);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpSerialNumber;
            _tmpSerialNumber = _cursor.getString(_cursorIndexOfSerialNumber);
            final long _tmpPurchaseDate;
            _tmpPurchaseDate = _cursor.getLong(_cursorIndexOfPurchaseDate);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final String _tmpCondition;
            _tmpCondition = _cursor.getString(_cursorIndexOfCondition);
            final String _tmpImagePath;
            if (_cursor.isNull(_cursorIndexOfImagePath)) {
              _tmpImagePath = null;
            } else {
              _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            }
            _item = new AssetEntity(_tmpAssetId,_tmpAssetName,_tmpCategory,_tmpSerialNumber,_tmpPurchaseDate,_tmpLocation,_tmpCondition,_tmpImagePath);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
