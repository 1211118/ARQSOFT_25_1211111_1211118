package pt.psoft.g1.psoftg1.usermanagement.api;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.shared.model.Name;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-23T09:45:17+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.39.0.v20240820-0604, environment: Java 17.0.12 (Eclipse Adoptium)"
)
@Component
public class UserViewMapperImpl extends UserViewMapper {

    @Override
    public UserView toUserView(User user) {
        if ( user == null ) {
            return null;
        }

        UserView userView = new UserView();

        userView.setFullName( userNameName( user ) );
        if ( user.getId() != null ) {
            userView.setId( String.valueOf( user.getId() ) );
        }
        userView.setUsername( user.getUsername() );

        return userView;
    }

    @Override
    public List<UserView> toUserView(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserView> list = new ArrayList<UserView>( users.size() );
        for ( User user : users ) {
            list.add( toUserView( user ) );
        }

        return list;
    }

    private String userNameName(User user) {
        if ( user == null ) {
            return null;
        }
        Name name = user.getName();
        if ( name == null ) {
            return null;
        }
        String name1 = name.getName();
        if ( name1 == null ) {
            return null;
        }
        return name1;
    }
}