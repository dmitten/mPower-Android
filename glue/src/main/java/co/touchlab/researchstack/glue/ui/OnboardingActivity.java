package co.touchlab.researchstack.glue.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import co.touchlab.researchstack.glue.R;
import co.touchlab.researchstack.glue.ResearchStack;
import co.touchlab.researchstack.glue.model.StudyOverviewModel;
import co.touchlab.researchstack.glue.task.SignInTask;
import co.touchlab.researchstack.glue.task.SignUpTask;
import co.touchlab.researchstack.glue.ui.adapter.OnboardingPagerAdapter;
import co.touchlab.researchstack.glue.ui.views.PageIndicator;
import co.touchlab.researchstack.glue.utils.JsonUtils;

/**
 * Created by bradleymcdermott on 10/15/15.
 */
public class OnboardingActivity extends AppCompatActivity
{
    public static final int REQUEST_CODE_SIGN_UP = 21473;
    public static final int REQUEST_CODE_SIGN_IN = 31473;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_onboarding);

        StudyOverviewModel model = parseStudyOverviewModel();
        OnboardingPagerAdapter adapter = new OnboardingPagerAdapter(this, model.getQuestions());

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(adapter);
//      TODO  pager.setPageTransformer();
//      TODO  pager.setPageMargin();

        final PageIndicator indicator = (PageIndicator) findViewById(R.id.pager_indicator);
        indicator.removeAllMarkers(true);
        indicator.addMarkers(adapter.getCount(),
                             R.drawable.ic_pageindicator_current_dark,
                             R.drawable.ic_pageindicator_default_dark,
                             true);

        pager.clearOnPageChangeListeners();
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                indicator.setActiveMarker(position);
            }
        });
    }

    //TODO Read on main thread for intense UI blockage.
    private StudyOverviewModel parseStudyOverviewModel()
    {
        int fileResId = ResearchStack.getInstance().getStudyOverviewResourceId();
        return JsonUtils.loadClassFromRawJson(OnboardingActivity.this, StudyOverviewModel.class, fileResId);
    }

    public void onSignUpClicked(View view)
    {
        SignUpTask task = new SignUpTask();
        startActivityForResult(SignUpTaskActivity.newIntent(this, task), REQUEST_CODE_SIGN_UP);
    }

    public void onSignInClicked(View view)
    {
        SignInTask task = new SignInTask();
        startActivityForResult(SignUpTaskActivity.newIntent(this, task), REQUEST_CODE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ((requestCode == REQUEST_CODE_SIGN_IN || requestCode == REQUEST_CODE_SIGN_UP) && resultCode == RESULT_OK)
        {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}