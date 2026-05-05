package com.servalabs.linkcheck.modules.list;

import static java.util.Objects.requireNonNullElse;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.servalabs.linkcheck.R;
import com.servalabs.linkcheck.activities.ModulesActivity;
import com.servalabs.linkcheck.dialogs.MainDialog;
import com.servalabs.linkcheck.modules.AModuleConfig;
import com.servalabs.linkcheck.modules.AModuleData;
import com.servalabs.linkcheck.modules.AModuleDialog;
import com.servalabs.linkcheck.modules.AutomationRules;
import com.servalabs.linkcheck.services.CustomTabs;
import com.servalabs.linkcheck.url.UrlData;
import com.servalabs.linkcheck.utilities.methods.AndroidUtils;
import com.servalabs.linkcheck.utilities.wrappers.IntentApp;

import java.util.List;

/**
 * This modules marks the insertion point of new modules
 * If enabled, shows a textview with debug info.
 * Allows also to enable/disable ctabs toasts
 */
public class DebugModule extends AModuleData {
    public static final String ID = "debug";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public int getName() {
        return R.string.mD_name;
    }

    @Override
    public boolean isEnabledByDefault() {
        return false;
    }

    @Override
    public AModuleDialog getDialog(MainDialog cntx) {
        return new DebugDialog(cntx);
    }

    @Override
    public AModuleConfig getConfig(ModulesActivity cntx) {
        return new DebugConfig(cntx);
    }

    @Override
    public List<AutomationRules.Automation<AModuleDialog>> getAutomations() {
        return (List<AutomationRules.Automation<AModuleDialog>>) (List<?>) DebugDialog.AUTOMATIONS;
    }
}

class DebugDialog extends AModuleDialog {

    static final List<AutomationRules.Automation<DebugDialog>> AUTOMATIONS = List.of(
            new AutomationRules.Automation<>(
                    "toast",
                    R.string.auto_toast,
                    ((t, args) -> Toast.makeText(t.getActivity(), args.optString("text"), Toast.LENGTH_SHORT).show())
            )
    );

    public static final String SEPARATOR = "";
    private TextView data;

    public DebugDialog(MainDialog dialog) {
        super(dialog);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_debug;
    }

    @Override
    public void onInitialize(View views) {
        data = views.findViewById(R.id.data);
        views.findViewById(R.id.showData).setOnClickListener(v -> showData());
    }

    private void showData() {
        data.setVisibility(View.VISIBLE);
        // data to display
        data.setText(String.join("\n", List.of(
                "Intent:",
                getActivity().getIntent().toUri(0),

                SEPARATOR,

                "queryIntentActivities:",
                IntentApp.getOtherPackages(new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl())), getActivity()).toString(),

                SEPARATOR,

                "queryIntentActivityOptions:",
                getActivity().getPackageManager().queryIntentActivityOptions(
                        new ComponentName(getActivity(), MainDialog.class.getName()),
                        null,
                        new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl())),
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PackageManager.MATCH_ALL : 0
                ).toString(),

                SEPARATOR,

                "UrlData:",
                getUrlData().toString(),

                SEPARATOR,

                "GlobalData:",
                getGlobalData().toString(),

                SEPARATOR,

                "Referrer:",
                requireNonNullElse(AndroidUtils.getReferrer(getActivity()), "null")
        )));
    }

    @Override
    public void onPrepareUrl(UrlData urlData) {
        data.setVisibility(View.GONE);
    }
}

class DebugConfig extends AModuleConfig {


    public DebugConfig(ModulesActivity activity) {
        super(activity);
    }

    @Override
    public int getLayoutId() {
        return R.layout.config_debug;
    }

    @Override
    public void onInitialize(View views) {
        CustomTabs.SHOWTOAST_PREF(getActivity())
                .attachToSwitch(views.findViewById(R.id.chk_ctabs));
    }
}
