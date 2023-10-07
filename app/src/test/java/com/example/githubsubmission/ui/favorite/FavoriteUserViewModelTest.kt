import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.example.githubsubmission.database.FavoriteUser
import com.example.githubsubmission.repository.FavoriteRepository
import com.example.githubsubmission.ui.favorite.FavoriteUserViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FavoriteUserViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var mApplication: Application

    @Mock
    lateinit var favoriteRepository: FavoriteRepository

    lateinit var favoriteUserViewModel: FavoriteUserViewModel

    @Before
    fun setUp() {
        mApplication = Application()
        MockitoAnnotations.openMocks(this)
        favoriteUserViewModel = FavoriteUserViewModel(mApplication)
        favoriteUserViewModel.mFavoriteRepository = favoriteRepository
    }

    @Test
    fun insert() {
        val testUser = FavoriteUser("testUsername", "Test User", 123123)

        // Mock the behavior of insert function in the repository
        Mockito.`when`(favoriteRepository.insert(testUser)).thenAnswer {
            // Do nothing for now, or you can return a specific value if needed
        }

        favoriteUserViewModel.insert(testUser)

        // Verify that the insert function was called in the repository
        Mockito.verify(favoriteRepository).insert(testUser)
    }

    @Test
    fun getAllFavoriteUser() {
        val dummyFavorites: LiveData<List<FavoriteUser>> = Mockito.mock(LiveData::class.java) as LiveData<List<FavoriteUser>>

        // Mock the behavior of getAllFavorites function in the repository
        Mockito.`when`(favoriteRepository.getAllFavorites()).thenReturn(dummyFavorites)

        // Call the function in the ViewModel
        val result = favoriteUserViewModel.getAllFavoriteUser()

        // Verify that the result matches the dummyFavorites
        assert(result == dummyFavorites)
    }

    // Add more tests as needed for other functions in FavoriteUserViewModel
}
