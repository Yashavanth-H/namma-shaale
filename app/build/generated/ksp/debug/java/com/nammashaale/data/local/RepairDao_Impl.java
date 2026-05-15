package com.nammashaale.data.local;

import android.database.Cursor;
import androidx.annotation.NonNull;
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
public final class RepairDao_Impl implements RepairDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RepairEntity> __insertionAdapterOfRepairEntity;

  private final EntityDeletionOrUpdateAdapter<RepairEntity> __updateAdapterOfRepairEntity;

  public RepairDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRepairEntity = new EntityInsertionAdapter<RepairEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `repairs` (`repairId`,`assetId`,`issue`,`status`,`requestDate`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RepairEntity entity) {
        statement.bindLong(1, entity.getRepairId());
        statement.bindLong(2, entity.getAssetId());
        statement.bindString(3, entity.getIssue());
        statement.bindString(4, entity.getStatus());
        statement.bindLong(5, entity.getRequestDate());
      }
    };
    this.__updateAdapterOfRepairEntity = new EntityDeletionOrUpdateAdapter<RepairEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `repairs` SET `repairId` = ?,`assetId` = ?,`issue` = ?,`status` = ?,`requestDate` = ? WHERE `repairId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RepairEntity entity) {
        statement.bindLong(1, entity.getRepairId());
        statement.bindLong(2, entity.getAssetId());
        statement.bindString(3, entity.getIssue());
        statement.bindString(4, entity.getStatus());
        statement.bindLong(5, entity.getRequestDate());
        statement.bindLong(6, entity.getRepairId());
      }
    };
  }

  @Override
  public Object insertRepair(final RepairEntity repair,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRepairEntity.insert(repair);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateRepair(final RepairEntity repair,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRepairEntity.handle(repair);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RepairEntity>> getAllRepairs() {
    final String _sql = "SELECT * FROM repairs ORDER BY requestDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"repairs"}, new Callable<List<RepairEntity>>() {
      @Override
      @NonNull
      public List<RepairEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRepairId = CursorUtil.getColumnIndexOrThrow(_cursor, "repairId");
          final int _cursorIndexOfAssetId = CursorUtil.getColumnIndexOrThrow(_cursor, "assetId");
          final int _cursorIndexOfIssue = CursorUtil.getColumnIndexOrThrow(_cursor, "issue");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfRequestDate = CursorUtil.getColumnIndexOrThrow(_cursor, "requestDate");
          final List<RepairEntity> _result = new ArrayList<RepairEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RepairEntity _item;
            final int _tmpRepairId;
            _tmpRepairId = _cursor.getInt(_cursorIndexOfRepairId);
            final int _tmpAssetId;
            _tmpAssetId = _cursor.getInt(_cursorIndexOfAssetId);
            final String _tmpIssue;
            _tmpIssue = _cursor.getString(_cursorIndexOfIssue);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpRequestDate;
            _tmpRequestDate = _cursor.getLong(_cursorIndexOfRequestDate);
            _item = new RepairEntity(_tmpRepairId,_tmpAssetId,_tmpIssue,_tmpStatus,_tmpRequestDate);
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
  public Flow<List<RepairEntity>> getRepairsByStatus(final String status) {
    final String _sql = "SELECT * FROM repairs WHERE status = ? ORDER BY requestDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, status);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"repairs"}, new Callable<List<RepairEntity>>() {
      @Override
      @NonNull
      public List<RepairEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRepairId = CursorUtil.getColumnIndexOrThrow(_cursor, "repairId");
          final int _cursorIndexOfAssetId = CursorUtil.getColumnIndexOrThrow(_cursor, "assetId");
          final int _cursorIndexOfIssue = CursorUtil.getColumnIndexOrThrow(_cursor, "issue");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfRequestDate = CursorUtil.getColumnIndexOrThrow(_cursor, "requestDate");
          final List<RepairEntity> _result = new ArrayList<RepairEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RepairEntity _item;
            final int _tmpRepairId;
            _tmpRepairId = _cursor.getInt(_cursorIndexOfRepairId);
            final int _tmpAssetId;
            _tmpAssetId = _cursor.getInt(_cursorIndexOfAssetId);
            final String _tmpIssue;
            _tmpIssue = _cursor.getString(_cursorIndexOfIssue);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpRequestDate;
            _tmpRequestDate = _cursor.getLong(_cursorIndexOfRequestDate);
            _item = new RepairEntity(_tmpRepairId,_tmpAssetId,_tmpIssue,_tmpStatus,_tmpRequestDate);
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
