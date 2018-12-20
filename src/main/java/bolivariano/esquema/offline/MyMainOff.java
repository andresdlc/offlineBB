/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bolivariano.esquema.offline;

import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.spring.Main;

/**
 * A Main to let you easily start the application from a IDE. Usually you can
 * just right click and choose Run
 *
 * @version
 */
public final class MyMainOff {

	private MyMainOff() {
		// to comply with check style
	}

	public static void main(String[] args) throws Exception {

		Main main = new Main();
		main.setApplicationContextUri("META-INF/spring/camel-config.xml");
		main.start();
		// DeRegistra el archivo de propiedades
		if (!main.getCamelContexts().isEmpty() && main.getCamelContexts().get(0).hasComponent("properties") != null) {
			main.getCamelContexts().get(0).removeComponent("properties");
		}

		// Registra el archivo de propiedades
		PropertiesComponent pc = new PropertiesComponent();
		//pc.setLocation("file:P:\\microservice\\OFFLINE\\common\\common.properties");
	    pc.setLocation("file:../../common/common.properties");
		main.getCamelContexts().get(0).addComponent("properties", pc);
	}
}
