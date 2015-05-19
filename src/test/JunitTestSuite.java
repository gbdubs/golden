package test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
   TestBag.class,
   TestGraphVisualization.class,
   TestIntToString.class,
   TestIsomorphism.class,
   TestNearlyCompleteGraphIsomorphism.class,
   TestStateEncoder.class
})

public class JunitTestSuite {   
}  	