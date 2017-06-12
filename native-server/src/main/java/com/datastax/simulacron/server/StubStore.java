package com.datastax.simulacron.server;

import com.datastax.oss.protocol.internal.Frame;
import com.datastax.simulacron.common.cluster.Node;
import com.datastax.simulacron.common.cluster.Scope;
import com.datastax.simulacron.common.stubbing.Action;
import com.datastax.simulacron.common.stubbing.StubMapping;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class StubStore {

  private final CopyOnWriteArrayList<StubMapping> stubMappings = new CopyOnWriteArrayList<>();

  StubStore() {}

  public void register(StubMapping mapping) {
    stubMappings.add(mapping);
  }

  public int clear(Scope scope, Class clazz) {
    Iterator<StubMapping> iterator = stubMappings.iterator();
    int count = 0;
    while (iterator.hasNext()) {
      StubMapping mapping = iterator.next();
      if (mapping.getClass().equals(clazz)) {
        if (scope.isScopeUnSet()) {
          stubMappings.remove(mapping);
          count++;
        } else {
          if (mapping.getScope().equals(scope)) {
            stubMappings.remove(mapping);
            count++;
          }
        }
      }
    }
    return count;
  }

  public void clear() {
    stubMappings.clear();
  }

  public List<Action> handle(Node node, Frame frame) {
    Optional<StubMapping> stubMapping = find(node, frame);
    if (stubMapping.isPresent()) {
      return stubMapping.get().getActions(node, frame);
    } else {
      return Collections.emptyList();
    }
  }

  Optional<StubMapping> find(Node node, Frame frame) {
    return stubMappings.stream().filter(s -> s.matches(node, frame)).findFirst();
  }
}
