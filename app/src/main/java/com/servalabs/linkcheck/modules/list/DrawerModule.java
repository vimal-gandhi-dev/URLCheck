package com.servalabs.linkcheck.modules.list;

import android.view.View;
import android.widget.ImageView;

import com.servalabs.linkcheck.R;
import com.servalabs.linkcheck.activities.ModulesActivity;
import com.servalabs.linkcheck.dialogs.MainDialog;
import com.servalabs.linkcheck.modules.AModuleConfig;
import com.servalabs.linkcheck.modules.AModuleData;
import com.servalabs.linkcheck.modules.AModuleDialog;
import com.servalabs.linkcheck.modules.AutomationRules;
import com.servalabs.linkcheck.modules.DescriptionConfig;

import java.util.List;

/** A special module that manages the drawer functionality */
public class DrawerModule extends AModuleData {
    @Override
    public String getId() {
        return "drawer";
    }

    @Override
    public int getName() {
        return R.string.mDrawer_name;
    }

    @Override
    public boolean isEnabledByDefault() {
        return false;
    }

    @Override
    public AModuleDialog getDialog(MainDialog cntx) {
        return new DrawerDialog(cntx);
    }

    @Override
    public AModuleConfig getConfig(ModulesActivity cntx) {
        return new DescriptionConfig(R.string.mDrawer_desc);
    }

    @Override
    public List<AutomationRules.Automation<AModuleDialog>> getAutomations() {
        return (List<AutomationRules.Automation<AModuleDialog>>) (List<?>) DrawerDialog.AUTOMATIONS;
    }
}

class DrawerDialog extends AModuleDialog {

    static final List<AutomationRules.Automation<DrawerDialog>> AUTOMATIONS = List.of(
            new AutomationRules.Automation<>("drawer", R.string.auto_drawer, dialog -> dialog.setDrawerVisibility(true))
    );

    private ImageView buttonL;
    private ImageView buttonR;

    public DrawerDialog(MainDialog dialog) {
        super(dialog);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_drawer;
    }

    @Override
    public void onInitialize(View views) {
        buttonL = views.findViewById(R.id.drawerL);
        buttonR = views.findViewById(R.id.drawerR);
        var parent = views.findViewById(R.id.parent);
        parent.getBackground().setAlpha(25);

        parent.setOnClickListener(v -> setDrawerVisibility(!getActivity().isDrawerVisible()));
        setDrawerVisibility(false);
    }

    public void setDrawerVisibility(boolean visible) {
        getActivity().setDrawerVisibility(visible);
        buttonL.setImageResource(visible ?
                R.drawable.arrow_down : R.drawable.arrow_right);
        buttonR.setImageResource(visible ?
                R.drawable.arrow_down : R.drawable.arrow_right);
    }

    @Override
    public void onFinishUrl() {
        setVisibility(getActivity().anyDrawerChildVisible());
    }

}