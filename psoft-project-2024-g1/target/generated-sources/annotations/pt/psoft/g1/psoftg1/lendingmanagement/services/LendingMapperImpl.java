package pt.psoft.g1.psoftg1.lendingmanagement.services;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-30T23:35:05+0000",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.40.0.v20240919-1711, environment: Java 17.0.12 (Eclipse Adoptium)"
)
@Component
public class LendingMapperImpl extends LendingMapper {

    @Override
    public void update(SetLendingReturnedRequest request, Lending lending) {
        if ( request == null ) {
            return;
        }

        lending.commentary = request.getCommentary();
    }
}
