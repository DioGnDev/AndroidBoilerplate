package ca.co.rufus.androidboilerplate.test.common.injection.module;

import android.app.Application;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import ca.co.rufus.androidboilerplate.ui.base.AppContainer;
import dagger.Module;
import dagger.Provides;
import ca.co.rufus.androidboilerplate.data.DataManager;
import ca.co.rufus.androidboilerplate.test.common.TestDataManager;

import static org.mockito.Mockito.spy;

/**
 * Provides application-level dependencies for an app running on a testing environment
 * This allows injecting mocks if necessary.
 */
@Module
public class ApplicationTestModule {
    private final Application mApplication;
    private boolean mMockableDataManager;

    public ApplicationTestModule(Application application, boolean mockableDataManager) {
        mApplication = application;
        mMockableDataManager = mockableDataManager;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    DataManager provideDataManager() {
        TestDataManager testDataManager = new TestDataManager(mApplication);
        return mMockableDataManager ? spy(testDataManager) : testDataManager;
    }

    @Provides
    @Singleton
    Bus provideEventBus() {
        return new Bus();
    }

    @Provides
    @Singleton
    AppContainer provideAppContainer() {
        return AppContainer.DEFAULT;
    }
}
