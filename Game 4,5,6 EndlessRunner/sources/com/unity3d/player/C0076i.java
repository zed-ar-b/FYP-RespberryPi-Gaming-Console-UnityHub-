package com.unity3d.player;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/* renamed from: com.unity3d.player.i */
public final class C0076i extends Dialog implements TextWatcher, View.OnClickListener {

    /* renamed from: d */
    private static int f232d = 1627389952;

    /* renamed from: e */
    private static int f233e = -1;

    /* renamed from: a */
    public boolean f234a;
    /* access modifiers changed from: private */

    /* renamed from: b */
    public Context f235b = null;
    /* access modifiers changed from: private */

    /* renamed from: c */
    public UnityPlayer f236c = null;

    /* renamed from: f */
    private int f237f;

    /* renamed from: g */
    private boolean f238g;

    /* renamed from: com.unity3d.player.i$a */
    private static final class C0081a {
        /* access modifiers changed from: private */

        /* renamed from: a */
        public static final int f244a = View.generateViewId();
        /* access modifiers changed from: private */

        /* renamed from: b */
        public static final int f245b = View.generateViewId();
        /* access modifiers changed from: private */

        /* renamed from: c */
        public static final int f246c = View.generateViewId();
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public C0076i(Context context, UnityPlayer unityPlayer, String str, int i, boolean z, boolean z2, boolean z3, String str2, int i2, boolean z4, boolean z5) {
        super(context);
        this.f235b = context;
        this.f236c = unityPlayer;
        Window window = getWindow();
        this.f234a = z5;
        window.requestFeature(1);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = 80;
        attributes.x = 0;
        attributes.y = 0;
        window.setAttributes(attributes);
        window.setBackgroundDrawable(new ColorDrawable(0));
        final View createSoftInputView = createSoftInputView();
        setContentView(createSoftInputView);
        window.setLayout(-1, -2);
        window.clearFlags(2);
        window.clearFlags(134217728);
        window.clearFlags(67108864);
        if (!this.f234a) {
            window.addFlags(32);
            window.addFlags(262144);
        }
        EditText editText = (EditText) findViewById(C0081a.f245b);
        m136a(editText, str, i, z, z2, z3, str2, i2);
        ((Button) findViewById(C0081a.f244a)).setOnClickListener(this);
        this.f237f = editText.getCurrentTextColor();
        mo296a(z4);
        this.f236c.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public final void onGlobalLayout() {
                if (createSoftInputView.isShown()) {
                    Rect rect = new Rect();
                    C0076i.this.f236c.getWindowVisibleDisplayFrame(rect);
                    int[] iArr = new int[2];
                    C0076i.this.f236c.getLocationOnScreen(iArr);
                    Point point = new Point(rect.left - iArr[0], rect.height() - createSoftInputView.getHeight());
                    Point point2 = new Point();
                    C0076i.this.getWindow().getWindowManager().getDefaultDisplay().getSize(point2);
                    int height = C0076i.this.f236c.getHeight() - point2.y;
                    int height2 = C0076i.this.f236c.getHeight() - point.y;
                    if (height2 != height + createSoftInputView.getHeight()) {
                        C0076i.this.f236c.reportSoftInputIsVisible(true);
                    } else {
                        C0076i.this.f236c.reportSoftInputIsVisible(false);
                    }
                    C0076i.this.f236c.reportSoftInputArea(new Rect(point.x, point.y, createSoftInputView.getWidth(), height2));
                }
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public final void onFocusChange(View view, boolean z) {
                if (z) {
                    C0076i.this.getWindow().setSoftInputMode(5);
                }
            }
        });
        editText.requestFocus();
    }

    /* renamed from: a */
    private static int m134a(int i, boolean z, boolean z2, boolean z3) {
        int i2 = 0;
        int i3 = (z ? 32768 : 524288) | (z2 ? 131072 : 0);
        if (z3) {
            i2 = 128;
        }
        int i4 = i3 | i2;
        if (i < 0 || i > 11) {
            return i4;
        }
        int[] iArr = {1, 16385, 12290, 17, 2, 3, 8289, 33, 1, 16417, 17, 8194};
        return (iArr[i] & 2) != 0 ? iArr[i] : iArr[i] | i4;
    }

    /* renamed from: a */
    private void m136a(EditText editText, String str, int i, boolean z, boolean z2, boolean z3, String str2, int i2) {
        editText.setImeOptions(6);
        editText.setText(str);
        editText.setHint(str2);
        editText.setHintTextColor(f232d);
        editText.setInputType(m134a(i, z, z2, z3));
        editText.setImeOptions(33554432);
        if (i2 > 0) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(i2)});
        }
        editText.addTextChangedListener(this);
        editText.setSelection(editText.getText().length());
        editText.setClickable(true);
    }

    /* access modifiers changed from: private */
    /* renamed from: a */
    public void m138a(String str, boolean z) {
        ((EditText) findViewById(C0081a.f245b)).setSelection(0, 0);
        this.f236c.reportSoftInputStr(str, 1, z);
    }

    /* access modifiers changed from: private */
    /* renamed from: b */
    public String m139b() {
        EditText editText = (EditText) findViewById(C0081a.f245b);
        if (editText == null) {
            return null;
        }
        return editText.getText().toString();
    }

    /* renamed from: a */
    public final String mo292a() {
        InputMethodSubtype currentInputMethodSubtype = ((InputMethodManager) this.f235b.getSystemService("input_method")).getCurrentInputMethodSubtype();
        if (currentInputMethodSubtype == null) {
            return null;
        }
        String locale = currentInputMethodSubtype.getLocale();
        if (locale != null && !locale.equals("")) {
            return locale;
        }
        String mode = currentInputMethodSubtype.getMode();
        String extraValue = currentInputMethodSubtype.getExtraValue();
        return mode + " " + extraValue;
    }

    /* renamed from: a */
    public final void mo293a(int i) {
        EditText editText = (EditText) findViewById(C0081a.f245b);
        if (editText == null) {
            return;
        }
        if (i > 0) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(i)});
            return;
        }
        editText.setFilters(new InputFilter[0]);
    }

    /* renamed from: a */
    public final void mo294a(int i, int i2) {
        int i3;
        EditText editText = (EditText) findViewById(C0081a.f245b);
        if (editText != null && editText.getText().length() >= (i3 = i2 + i)) {
            editText.setSelection(i, i3);
        }
    }

    /* renamed from: a */
    public final void mo295a(String str) {
        EditText editText = (EditText) findViewById(C0081a.f245b);
        if (editText != null) {
            editText.setText(str);
            editText.setSelection(str.length());
        }
    }

    /* renamed from: a */
    public final void mo296a(boolean z) {
        this.f238g = z;
        EditText editText = (EditText) findViewById(C0081a.f245b);
        Button button = (Button) findViewById(C0081a.f244a);
        View findViewById = findViewById(C0081a.f246c);
        if (z) {
            editText.setBackgroundColor(0);
            editText.setTextColor(0);
            editText.setCursorVisible(false);
            editText.setHighlightColor(0);
            editText.setOnClickListener(this);
            editText.setLongClickable(false);
            button.setTextColor(0);
            findViewById.setBackgroundColor(0);
            findViewById.setOnClickListener(this);
            return;
        }
        editText.setBackgroundColor(f233e);
        editText.setTextColor(this.f237f);
        editText.setCursorVisible(true);
        editText.setOnClickListener((View.OnClickListener) null);
        editText.setLongClickable(true);
        button.setClickable(true);
        button.setTextColor(this.f237f);
        findViewById.setBackgroundColor(f233e);
        findViewById.setOnClickListener((View.OnClickListener) null);
    }

    public final void afterTextChanged(Editable editable) {
        this.f236c.reportSoftInputStr(editable.toString(), 0, false);
        EditText editText = (EditText) findViewById(C0081a.f245b);
        int selectionStart = editText.getSelectionStart();
        this.f236c.reportSoftInputSelection(selectionStart, editText.getSelectionEnd() - selectionStart);
    }

    public final void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    /* access modifiers changed from: protected */
    public final View createSoftInputView() {
        RelativeLayout relativeLayout = new RelativeLayout(this.f235b);
        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        relativeLayout.setBackgroundColor(f233e);
        relativeLayout.setId(C0081a.f246c);
        C00793 r1 = new EditText(this.f235b) {
            public final boolean onKeyPreIme(int i, KeyEvent keyEvent) {
                if (i == 4) {
                    C0076i iVar = C0076i.this;
                    iVar.m138a(iVar.m139b(), true);
                    return true;
                } else if (i == 84) {
                    return true;
                } else {
                    return super.onKeyPreIme(i, keyEvent);
                }
            }

            /* access modifiers changed from: protected */
            public final void onSelectionChanged(int i, int i2) {
                C0076i.this.f236c.reportSoftInputSelection(i, i2 - i);
            }

            public final void onWindowFocusChanged(boolean z) {
                super.onWindowFocusChanged(z);
                if (z) {
                    ((InputMethodManager) C0076i.this.f235b.getSystemService("input_method")).showSoftInput(this, 0);
                }
            }
        };
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(15);
        layoutParams.addRule(0, C0081a.f244a);
        r1.setLayoutParams(layoutParams);
        r1.setId(C0081a.f245b);
        relativeLayout.addView(r1);
        Button button = new Button(this.f235b);
        button.setText(this.f235b.getResources().getIdentifier("ok", "string", "android"));
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams2.addRule(15);
        layoutParams2.addRule(11);
        button.setLayoutParams(layoutParams2);
        button.setId(C0081a.f244a);
        button.setBackgroundColor(0);
        relativeLayout.addView(button);
        ((EditText) relativeLayout.findViewById(C0081a.f245b)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 6) {
                    C0076i iVar = C0076i.this;
                    iVar.m138a(iVar.m139b(), false);
                }
                return false;
            }
        });
        relativeLayout.setPadding(16, 16, 16, 16);
        return relativeLayout;
    }

    public final boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.f234a || (motionEvent.getAction() != 4 && !this.f238g)) {
            return super.dispatchTouchEvent(motionEvent);
        }
        return true;
    }

    public final void onBackPressed() {
        m138a(m139b(), true);
    }

    public final void onClick(View view) {
        m138a(m139b(), false);
    }

    public final void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }
}
