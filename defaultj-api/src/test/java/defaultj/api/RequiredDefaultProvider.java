package defaultj.api;

import defaultj.api.IProvideDefault;
import defaultj.api.ProvideDefaultException;

public class RequiredDefaultProvider implements IProvideDefault {
    
    @Override
    public <TYPE> TYPE get(Class<TYPE> theGivenClass) throws ProvideDefaultException {
        // TODO Auto-generated method stub
        return null;
    }
    
}