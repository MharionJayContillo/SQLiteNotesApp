import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES)
        onCreate(db)
    }

    fun insertNote(title: String?, content: String?): Long {
        val db = this.getWritableDatabase()
        val values = ContentValues()
        values.put(COLUMN_TITLE, title)
        values.put(COLUMN_CONTENT, content)
        val newRowId = db.insert(TABLE_NOTES, null, values)
        db.close()
        return newRowId
    }

    fun deleteNote(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_NOTES, "$COLUMN_ID = ?", arrayOf(id.toString()))

    }

    val allNotes: MutableList<Note>
        get() {
            val notesList: MutableList<Note> = ArrayList()
            val selectQuery = "SELECT * FROM $TABLE_NOTES"
            val db = readableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                    val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
                    notesList.add(Note(id, title, content))
                } while (cursor.moveToNext())
            }

            cursor.close()
            db.close()
            return notesList
        }



    companion object {
        private const val DATABASE_NAME = "MyNotes.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NOTES: String = "notes"
        const val COLUMN_ID: String = "_id"
        const val COLUMN_TITLE: String = "title"
        const val COLUMN_CONTENT: String = "content"

        private val SQL_CREATE_TABLE_NOTES = "CREATE TABLE " + TABLE_NOTES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + " TEXT NOT NULL," +
                COLUMN_CONTENT + " TEXT);"
    }
}

data class Note(val id: Int, val title: String, val content: String) {
    override fun toString(): String {
        return "$title - $content"
    }
}
