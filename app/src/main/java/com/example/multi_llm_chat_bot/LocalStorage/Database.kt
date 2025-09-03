package com.example.multi_llm_chat_bot.LocalStorage

import android.content.Context
//import androidx.privacysandbox.tools.core.generator.build
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase



@Database(
    entities = [Conversation::class, ChatMessage::class],
    version = 1, // Start with version 1. Increment if you change the schema later.
    exportSchema = false // Set to true if you want to export schema to a folder (good for complex projects)
)
// @TypeConverters(Converters::class) // Uncomment if you created and need Converters.kt
abstract class AppDatabase : RoomDatabase() {

    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile // Ensures visibility of this instance across threads
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chatbot_database" // Name of your database file
                )
                    // .addMigrations(MIGRATION_1_2, ...) // Add migrations if you update the version
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}