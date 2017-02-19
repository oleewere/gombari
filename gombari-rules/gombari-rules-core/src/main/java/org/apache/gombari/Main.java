/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.gombari;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    KieServices kieServices = KieServices.Factory.get();
    KieRepository kieRepository = kieServices.getRepository();
    KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

    kieFileSystem.write(ResourceFactory.newClassPathResource("rules/dummy.drl", kieFileSystem.getClass()));

    KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);

    kb.buildAll();

    KieContainer kContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId());

    KieSession kSession = kContainer.newKieSession();

    List<String> list = new ArrayList<>();
    list.add("dummyListElement");
    kSession.setGlobal("list", list);

    kSession.addEventListener(new DebugAgendaEventListener());
    kSession.addEventListener(new DebugRuleRuntimeEventListener());

    final DummyObject dummyObject = new DummyObject();
    dummyObject.setMessage("myMessage");

    kSession.insert(dummyObject);

    kSession.fireAllRules();

  }

  public static class DummyObject {
    private String message;

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }
}