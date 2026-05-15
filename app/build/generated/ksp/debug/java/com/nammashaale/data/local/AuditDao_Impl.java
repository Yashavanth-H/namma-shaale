package com.nammashaale.data.local;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
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
public final class AuditDao_Impl implements AuditDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AuditEntity> __insertionAdapterOfAuditEntity;

  public AuditDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAuditEntity = new EntityInsertionAdapter<AuditEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `audits` (`auditId`,`assetId`,`status`,`checkedDate`,`remarks`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AuditEntity entity) {
        statement.bindLong(1, entity.getAuditId());
        statement.bindLong(2, entity.getAssetId());
        statement.bindString(3, entity.getStatus());
        statement.bindLong(4, entity.getCheckedDate());
        if (entity.getRemarks() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getRemarks());
        }
      }
    };
  }

  @Override
  public Object insertAudit(final AuditEntity audit, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAuditEntity.insert(audit);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AuditEntity>> getAllAudits() {
    final String _sql = "SELECT * FROM audits ORDER BY checkedDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audits"}, new Callable<List<AuditEntity>>() {
      @Override
      @NonNull
      public List<AuditEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfAuditId = CursorUtil.getColumnIndexOrThrow(_cursor, "auditId");
          final int _cursorIndexOfAssetId = CursorUtil.getColumnIndexOrThrow(_cursor, "assetId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCheckedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkedDate");
          final int _cursorIndexOfRemarks = CursorUtil.getColumnIndexOrThrow(_cursor, "remarks");
          final List<AuditEntity> _result = new ArrayList<AuditEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AuditEntity _item;
            final int _tmpAuditId;
            _tmpAuditId = _cursor.getInt(_cursorIndexOfAuditId);
            final int _tmpAssetId;
            _tmpAssetId = _cursor.getInt(_cursorIndexOfAssetId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpCheckedDate;
            _tmpCheckedDate = _cursor.getLong(_cursorIndexOfCheckedDate);
            final String _tmpRemarks;
            if (_cursor.isNull(_cursorIndexOfRemarks)) {
              _tmpRemarks = null;
            } else {
              _tmpRemarks = _cursor.getString(_cursorIndexOfRemarks);
            }
            _item = new AuditEntity(_tmpAuditId,_tmpAssetId,_tmpStatus,_tmpCheckedDate,_tmpRemarks);
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
  public Flow<List<AuditEntity>> getAuditsForAsset(final int assetId) {
    final String _sql = "SELECT * FROM audits WHERE assetId = ? ORDER BY checkedDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, assetId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audits"}, new Callable<List<AuditEntity>>() {
      @Override
      @NonNull
      public List<AuditEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfAuditId = CursorUtil.getColumnIndexOrThrow(_cursor, "auditId");
          final int _cursorIndexOfAssetId = CursorUtil.getColumnIndexOrThrow(_cursor, "assetId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCheckedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkedDate");
          final int _cursorIndexOfRemarks = CursorUtil.getColumnIndexOrThrow(_cursor, "remarks");
          final List<AuditEntity> _result = new ArrayList<AuditEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AuditEntity _item;
            final int _tmpAuditId;
            _tmpAuditId = _cursor.getInt(_cursorIndexOfAuditId);
            final int _tmpAssetId;
            _tmpAssetId = _cursor.getInt(_cursorIndexOfAssetId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpCheckedDate;
            _tmpCheckedDate = _cursor.getLong(_cursorIndexOfCheckedDate);
            final String _tmpRemarks;
            if (_cursor.isNull(_cursorIndexOfRemarks)) {
              _tmpRemarks = null;
            } else {
              _tmpRemarks = _cursor.getString(_cursorIndexOfRemarks);
            }
            _item = new AuditEntity(_tmpAuditId,_tmpAssetId,_tmpStatus,_tmpCheckedDate,_tmpRemarks);
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
