class Resource {
  constructor(
    type,
    acquisitionClass,
    acquisitionMethod,
    releaseClass,
    releaseMethod,
    isReleasedMethod,
    singleInstance,
    hasDependencies,
  ) {
    this.type = type;
    this.acquisitionClass = acquisitionClass;
    this.acquisitionMethod = acquisitionMethod;
    this.releaseClass = releaseClass;
    this.releaseMethod = releaseMethod;
    this.isReleasedMethod = isReleasedMethod;
    this.singleInstance = singleInstance;
    this.hasDependencies = hasDependencies;
  }
}

class Resources {
  constructor() {
    this.resources = [
      new Resource(
        "Landroid/database/sqlite/SQLiteDatabase;",
        ["Landroid/database/sqlite/SQLiteOpenHelper;"],
        ["getReadableDatabase", "getWritableDatabase"],
        [
          "Landroid/database/sqlite/SQLiteClosable;",
          "Landroid/database/sqlite/SQLiteDatabase;",
          "Landroid/database/sqlite/SQLiteOpenHelper;",
        ],
        ["close"],
        "!isOpen",
        true,
        true,
      ),
      new Resource(
        "Landroid/database/sqlite/SQLiteDatabase;",
        [
          "Landroid/database/sqlite/SQLiteDatabase;",
          "Landroid/content/Context;",
        ],
        ["openOrCreateDatabase"],
        [
          "Landroid/database/sqlite/SQLiteClosable;",
          "Landroid/database/sqlite/SQLiteDatabase;",
          "Landroid/database/sqlite/SQLiteOpenHelper;",
        ],
        ["close"],
        "!isOpen",
        false,
        true,
      ),
      new Resource(
        "Landroid/database/Cursor;",
        [
          "Landroid/database/sqlite/SQLiteDatabase;",
          "Landroid/content/ContentResolver;",
        ],
        ["query", "rawQuery", "queryWithFactory", "rawQueryWithFactory"],
        [
          "Landroid/database/Cursor;",
          "Landroid/app/Activity;",
          "Landroid/app/ListActivity;",
        ],
        ["close", "startManagingCursor"],
        "isClosed",
        false,
        false,
      ),
      new Resource(
        "Ljava/io/InputStream;",
        [
          "Landroid/content/ContentResolver;",
          "Landroid/content/Context;",
          "Landroid/content/res/Resources;",
          "Landroid/content/res/AssetManager;",
          "Ljava/net/URL;",
        ],
        [
          "openInputStream",
          "openFileInput",
          "openRawResource",
          "open",
          "openStream",
        ],
        ["Ljava/io/InputStream;"],
        ["close"],
        "",
        false,
        false,
      ),
    ];
  }

  getAllResources() {
    return this.resources;
  }
}

export default Resources;
