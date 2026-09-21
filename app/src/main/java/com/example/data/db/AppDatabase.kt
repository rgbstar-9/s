package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.EmergencyIncidentEntity
import com.example.data.model.FirstAidItemEntity
import com.example.data.model.SupplyRequisitionEntity
import com.example.data.model.TrafficBoothEntity

@Database(
  entities = [
    TrafficBoothEntity::class,
    FirstAidItemEntity::class,
    EmergencyIncidentEntity::class,
    SupplyRequisitionEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun firstAidDao(): FirstAidDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "sanjeevani_first_aid.db"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}
