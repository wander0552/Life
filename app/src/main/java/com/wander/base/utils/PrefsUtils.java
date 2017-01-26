package com.wander.base.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * DefaultSharedPreferences
 */
@SuppressLint("UseValueOf")
public class PrefsUtils {
    private static SharedPreferences pref = null;
    private static SharedPreferences.Editor editor = null;

    public static String loadPrefString(Context context, String key) {
        return loadPrefString(context, key, null);
    }

    public static String loadPrefString(Context context, String key, String defaultValue) {
        if (pref == null) {
            pref = PreferenceManager.getDefaultSharedPreferences(context);
        }
        String strRet = defaultValue;
        try {
            strRet = pref.getString(key, defaultValue);
        } catch (ClassCastException e) {
            // 一定是另一种类型
            strRet = defaultValue;

            boolean bLong = true;
            boolean bInt = true;
            boolean bBool = true;
            boolean bFloat = true;

            long lVal = 0;
            int iVal = 0;
            boolean bVal = false;
            float fVal = 0f;

            try {
                lVal = pref.getLong(key, 0);
            } catch (ClassCastException e2) {
                bLong = false;
                try {
                    iVal = pref.getInt(key, 0);
                } catch (ClassCastException e3) {
                    bInt = false;
                    try {
                        bVal = pref.getBoolean(key, false);
                    } catch (ClassCastException e4) {
                        bBool = false;
                        try {
                            fVal = pref.getFloat(key, 0f);
                        } catch (ClassCastException e5) {
                            bFloat = false;
                        }
                    }
                }
            }
            if (bLong) {
                strRet = new Long(lVal).toString();
            } else if (bInt) {
                strRet = new Integer(iVal).toString();
            } else if (bBool) {
                strRet = bVal ? "1" : "0";
            } else if (bFloat) {
                strRet = new Float(fVal).toString();
            }
        }
        return strRet;
    }

    public static void savePrefString(Context context, String key, String value) {
        if (pref == null) {
            pref = PreferenceManager.getDefaultSharedPreferences(context);
        }
        if (editor == null) {
            editor = pref.edit();
        }
        editor.putString(key, value);
        editor.apply();
    }

    public static int loadPrefInt(Context context, String key, int defaultValue) {
        if (pref == null) {
            pref = PreferenceManager.getDefaultSharedPreferences(context);
        }

        int iRet = defaultValue;
        try {
            iRet = pref.getInt(key, defaultValue);
        } catch (ClassCastException e) {
            // 一定是另一种类型
            iRet = defaultValue;

            boolean bStr = true;
            boolean bLong = true;
            boolean bBool = true;
            boolean bFloat = true;

            String strVal = "";
            long lVal = 0;
            boolean bVal = false;
            float fVal = 0f;

            try {
                strVal = pref.getString(key, "");
            } catch (ClassCastException e3) {
                bStr = false;
                try {
                    lVal = pref.getLong(key, 0);
                } catch (ClassCastException e2) {
                    bLong = false;
                    try {
                        bVal = pref.getBoolean(key, false);
                    } catch (ClassCastException e4) {
                        bBool = false;
                        try {
                            fVal = pref.getFloat(key, 0f);
                        } catch (ClassCastException e5) {
                            bFloat = false;
                        }
                    }
                }
            }

            if (bStr) {
                try {
                    iRet = Integer.parseInt(strVal);
                } catch (NumberFormatException e2) {
                    iRet = defaultValue;
                }
            } else if (bLong) {
                iRet = (int) (lVal);
            } else if (bBool) {
                iRet = bVal ? 1 : 0;
            } else if (bFloat) {
                iRet = (int) (fVal);
            }
        }

        return iRet;
    }

    public static void savePrefInt(Context context, String key, int value) {
        if (pref == null) {
            pref = PreferenceManager.getDefaultSharedPreferences(context);
        }
        if (editor == null) {
            editor = pref.edit();
        }
        editor.putInt(key, value);
        editor.apply();
    }

    public static long loadPrefLong(Context context, String key, long defaultValue) {
        if (pref == null) {
            pref = PreferenceManager.getDefaultSharedPreferences(context);
        }

        long lRet = defaultValue;
        try {
            lRet = pref.getLong(key, defaultValue);
        } catch (ClassCastException e) {
            // 一定是另一种类型
            lRet = defaultValue;

            boolean bStr = true;
            boolean bInt = true;
            boolean bBool = true;
            boolean bFloat = true;

            String strVal = "";
            int iVal = 0;
            boolean bVal = false;
            float fVal = 0f;

            try {
                strVal = pref.getString(key, "");
            } catch (ClassCastException e2) {
                bStr = false;
                try {
                    iVal = pref.getInt(key, 0);
                } catch (ClassCastException e3) {
                    bInt = false;
                    try {
                        bVal = pref.getBoolean(key, false);
                    } catch (ClassCastException e4) {
                        bBool = false;
                        try {
                            fVal = pref.getFloat(key, 0f);
                        } catch (ClassCastException e5) {
                            bFloat = false;
                        }
                    }
                }
            }
            if (bStr) {
                try {
                    lRet = Long.parseLong(strVal);
                } catch (NumberFormatException e2) {
                    lRet = defaultValue;
                }

            } else if (bInt) {
                lRet = (long) (iVal);
            } else if (bBool) {
                lRet = bVal ? 1 : 0;
            } else if (bFloat) {
                lRet = (long) (fVal);
            }
        }

        return lRet;
    }

    public static void savePrefLong(Context context, String key, long value) {
        if (pref == null) {
            pref = PreferenceManager.getDefaultSharedPreferences(context);
        }
        if (editor == null) {
            editor = pref.edit();
        }
        editor.putLong(key, value);
        editor.apply();
    }

    public static float loadPrefFloat(Context context, String key, float defaultValue) {
        if (pref == null) {
            pref = PreferenceManager.getDefaultSharedPreferences(context);
        }

        float fRet = defaultValue;
        try {
            fRet = pref.getFloat(key, defaultValue);
        } catch (ClassCastException e) {
            // 一定是另一种类型
            fRet = defaultValue;

            boolean bStr = true;
            boolean bLong = true;
            boolean bInt = true;
            boolean bBool = true;

            String strVal = "";
            long lVal = 0;
            int iVal = 0;
            boolean bVal = false;

            try {
                strVal = pref.getString(key, "");
            } catch (ClassCastException e1) {
                bStr = false;
                try {
                    lVal = pref.getLong(key, 0);
                } catch (ClassCastException e2) {
                    bLong = false;
                    try {
                        iVal = pref.getInt(key, 0);
                    } catch (ClassCastException e3) {
                        bInt = false;
                        try {
                            bVal = pref.getBoolean(key, false);
                        } catch (ClassCastException e4) {
                            bBool = false;
                        }
                    }
                }
            }

            if (bStr) {
                try {
                    fRet = Float.parseFloat(strVal);
                } catch (NumberFormatException e2) {
                    fRet = defaultValue;
                }

            } else if (bLong) {
                fRet = (float) lVal;
            } else if (bInt) {
                fRet = (float) iVal;
            } else if (bBool) {
                fRet = bVal ? 1.0f : 0.0f;
            }
        }

        return fRet;
    }

    public static void savePrefFloat(Context context, String key, float value) {
        if (pref == null) {
            pref = PreferenceManager.getDefaultSharedPreferences(context);
        }
        if (editor == null) {
            editor = pref.edit();
        }
        editor.putFloat(key, value);
        editor.apply();
    }

    public static boolean loadPrefBoolean(Context context, String key, boolean defaultValue) {
        if (pref == null) {
            pref = PreferenceManager.getDefaultSharedPreferences(context);
        }

        boolean bRet = defaultValue;
        try {
            bRet = pref.getBoolean(key, defaultValue);
        } catch (ClassCastException e) {
            // 一定是另一种类型
            bRet = defaultValue;

            boolean bStr = true;
            boolean bLong = true;
            boolean bInt = true;
            boolean bFloat = true;

            String strVal = "";
            long lVal = 0;
            int iVal = 0;
            float fVal = 0f;

            try {
                strVal = pref.getString(key, "");
            } catch (ClassCastException e1) {
                bStr = false;
                try {
                    lVal = pref.getLong(key, 0);
                } catch (ClassCastException e2) {
                    bLong = false;
                    try {
                        iVal = pref.getInt(key, 0);
                    } catch (ClassCastException e3) {
                        bInt = false;
                        try {
                            fVal = pref.getFloat(key, 0f);
                        } catch (ClassCastException e4) {
                            bFloat = false;
                        }
                    }
                }
            }

            if (bStr) {
                strVal = strVal.toLowerCase();
                bRet = (strVal.equals("true") || strVal.equals("1"));
            } else if (bLong) {
                bRet = (lVal == 1);
            } else if (bInt) {
                bRet = (iVal == 1);
            } else if (bFloat) {
                bRet = !(fVal < 1e-14);
            }
        }
        return bRet;
    }

    public static void savePrefBoolean(Context context, String key, boolean value) {
        if (pref == null) {
            pref = PreferenceManager.getDefaultSharedPreferences(context);
        }
        if (editor == null) {
            editor = pref.edit();
        }
        editor.putBoolean(key, value);
        editor.apply();
    }
}
