package co.com.pragma.r2dbc.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MariaDBConnectionPropertiesTest {

    @Test
    void shouldBindProperties() {
        MockEnvironment environment = new MockEnvironment();
        environment.setProperty("adapters.r2dbc.host", "localhost");
        environment.setProperty("adapters.r2dbc.port", "3306");
        environment.setProperty("adapters.r2dbc.database", "mydb");
        environment.setProperty("adapters.r2dbc.username", "root");
        environment.setProperty("adapters.r2dbc.password", "secret");

        Binder binder = Binder.get(environment);
        MariaDBConnectionProperties props = binder.bind("adapters.r2dbc",
                Bindable.of(MariaDBConnectionProperties.class)).get();

        assertThat(props.host()).isEqualTo("localhost");
        assertThat(props.port()).isEqualTo(3306);
        assertThat(props.database()).isEqualTo("mydb");
        assertThat(props.username()).isEqualTo("root");
        assertThat(props.password()).isEqualTo("secret");
    }

}