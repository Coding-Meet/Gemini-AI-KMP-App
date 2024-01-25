import data.respository.repositoryModule
import domain.di.domainModule
import org.koin.dsl.module
import presenation.di.screenViewModel
import utils.AppCoroutineDispatchers

val appModule = module {
    single<AppCoroutineDispatchers> { AppCoroutineDispatchersImpl() }
    includes(screenViewModel,domainModule, repositoryModule)

}
