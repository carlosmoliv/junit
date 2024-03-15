package org.example;

import org.example.User;
import org.example.utils.Resources;
import org.example.utils.Xml;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.xmlunit.assertj.XmlAssert;
import org.junit.jupiter.params.ParameterizedTest;
import static org.assertj.core.api.Assertions.assertThat;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Tag("unit")
public class UserTest {
    User user;

    @BeforeEach
    void setup() {
        user = new User("Jane", 30, false, LocalDate.of(1993, 6, 21));
    }

    @AfterEach
    void cleanup() {
        user = null;
    }

    @Test
    public void user_with_less_than_18_is_invalid() {
        assertThat(user.age()).isGreaterThanOrEqualTo(18);
    }

    @Test
    public void json_example() {
        assertThatJson(user).isEqualTo("{\"name\":\"Jane\",\"age\":30,\"blocked\":false,\"birthDate\":[1993, 6, 21]}");
    }

    @Test
    public void xml_example() {
        XmlAssert.assertThat( "<a><b attr=\"abc\"></b></a>").nodesByXPath("//a/b/@attr").exist();
    }

    @ParameterizedTest
    @ValueSource(ints = {20, 30, 40})
    public void parameterizedWithIntExample(int age) {
        assertThat(age).isGreaterThanOrEqualTo(18);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/users.csv", numLinesToSkip = 1)
    public void parameterizedWithCsvExample(String name, int age) {
        assertThat(age).isGreaterThanOrEqualTo(18);
    }

    @TestFactory
    Collection<DynamicTest> testFactoryExample() {
        List<Xml> xmlList = Resources.collectXmlResourcesMatchingPattern("users.*\\.xml");
        return xmlList.stream().map(xml -> DynamicTest.dynamicTest(xml.name(), () -> {
            XmlAssert.assertThat(xml.content()).hasXPath("/users/user/name");
        })).collect(Collectors.toList());
    }
}
