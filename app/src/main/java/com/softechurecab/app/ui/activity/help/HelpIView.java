package com.softechurecab.app.ui.activity.help;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.Help;

public interface HelpIView extends MvpView {

    void onSuccess(Help help);

    void onError(Throwable e);
}
