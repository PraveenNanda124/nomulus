// Copyright 2017 The Nomulus Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package google.registry.rde;

import static com.google.common.truth.Truth.assertThat;
import static google.registry.testing.DatabaseHelper.loadRegistrar;
import static google.registry.xml.ValidationMode.STRICT;

import google.registry.testing.AppEngineExtension;
import google.registry.xml.XmlTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

/** Unit tests for {@link RdeMarshaller}. */
public class RdeMarshallerTest {

  private static final String DECLARATION =
      "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";

  @RegisterExtension
  public final AppEngineExtension appEngine = AppEngineExtension.builder().withCloudSql().build();

  @Test
  void testMarshalRegistrar_validData_producesXmlFragment() throws Exception {
    DepositFragment fragment =
        new RdeMarshaller(STRICT).marshalRegistrar(loadRegistrar("TheRegistrar"));
    assertThat(fragment.type()).isEqualTo(RdeResourceType.REGISTRAR);
    assertThat(fragment.error()).isEmpty();
    String expected = ""
        + "<rdeRegistrar:registrar>\n"
        + "    <rdeRegistrar:id>TheRegistrar</rdeRegistrar:id>\n"
        + "    <rdeRegistrar:name>The Registrar</rdeRegistrar:name>\n"
        + "    <rdeRegistrar:gurid>1</rdeRegistrar:gurid>\n"
        + "    <rdeRegistrar:status>ok</rdeRegistrar:status>\n"
        + "    <rdeRegistrar:postalInfo type=\"loc\">\n"
        + "        <rdeRegistrar:addr>\n"
        + "            <rdeRegistrar:street>123 Example Bőulevard</rdeRegistrar:street>\n"
        + "            <rdeRegistrar:city>Williamsburg</rdeRegistrar:city>\n"
        + "            <rdeRegistrar:sp>NY</rdeRegistrar:sp>\n"
        + "            <rdeRegistrar:pc>11211</rdeRegistrar:pc>\n"
        + "            <rdeRegistrar:cc>US</rdeRegistrar:cc>\n"
        + "        </rdeRegistrar:addr>\n"
        + "    </rdeRegistrar:postalInfo>\n"
        + "    <rdeRegistrar:postalInfo type=\"int\">\n"
        + "        <rdeRegistrar:addr>\n"
        + "            <rdeRegistrar:street>123 Example Boulevard</rdeRegistrar:street>\n"
        + "            <rdeRegistrar:city>Williamsburg</rdeRegistrar:city>\n"
        + "            <rdeRegistrar:sp>NY</rdeRegistrar:sp>\n"
        + "            <rdeRegistrar:pc>11211</rdeRegistrar:pc>\n"
        + "            <rdeRegistrar:cc>US</rdeRegistrar:cc>\n"
        + "        </rdeRegistrar:addr>\n"
        + "    </rdeRegistrar:postalInfo>\n"
        + "    <rdeRegistrar:voice>+1.2223334444</rdeRegistrar:voice>\n"
        + "    <rdeRegistrar:email>the.registrar@example.com</rdeRegistrar:email>\n"
        + "    <rdeRegistrar:url>http://my.fake.url</rdeRegistrar:url>\n"
        + "    <rdeRegistrar:whoisInfo>\n"
        + "        <rdeRegistrar:name>whois.nic.fakewhois.example</rdeRegistrar:name>\n"
        + "    </rdeRegistrar:whoisInfo>\n"
        + "    <rdeRegistrar:crDate>mine eyes have seen the glory</rdeRegistrar:crDate>\n"
        + "    <rdeRegistrar:upDate>of the coming of the borg</rdeRegistrar:upDate>\n"
        + "</rdeRegistrar:registrar>\n";
    XmlTestUtils.assertXmlEquals(DECLARATION + expected, DECLARATION + fragment.xml(),
        "registrar.crDate",
        "registrar.upDate");
  }

  @Test
  void testMarshalRegistrar_unicodeCharacters_dontGetMangled() {
    DepositFragment fragment =
        new RdeMarshaller(STRICT).marshalRegistrar(loadRegistrar("TheRegistrar"));
    assertThat(fragment.xml()).contains("123 Example Bőulevard");
  }
}
