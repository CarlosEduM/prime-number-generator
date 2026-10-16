---
name: java-unit-testing
description: "Create, improve, or review Java unit tests. Use when writing JUnit tests, choosing test cases, using Mockito or test doubles, or validating Java behavior with focused tests."
argument-hint: "Describe the Java unit or behavior to test"
---

# Java Unit Testing

Create readable, isolated Java unit tests that describe observable behavior and catch meaningful regressions. Follow the project's test framework and conventions; use the practices below as a guide, not as a reason to introduce new dependencies or rewrite existing tests unnecessarily.

## When to Use

- Add or update unit tests for Java classes and methods.
- Review tests for isolation, clarity, useful coverage, and reliability.
- Decide between a real collaborator, fake, stub, or mock.

## Procedure

1. **Inspect the local testing setup.** Read applicable repository instructions, the target class, its callers or collaborators when relevant, nearby tests, build configuration, and test commands. Reuse the project's JUnit version, assertion library, naming style, and test utilities. Place tests under the matching `src/test/java` package, separate from production sources. For a new Java project without established conventions, prefer JUnit 5 and Mockito when available. Check that the Mockito JUnit Jupiter extension is present before using it; avoid adding dependencies unless the task requires them.

2. **Define the behavior contract.** Identify the public behavior under test: inputs, returned values, observable state changes, collaborator effects, and documented failures. Test through the public API rather than private methods or implementation details. Keep each test focused on one behavior or closely related outcome.

3. **Choose discriminating, realistic cases.** Cover normal production scenarios and boundaries or invalid inputs that could change the result. Include error cases only when they are part of the contract. Prefer a small set of cases that would fail if the behavior regressed over a large set of redundant examples. Use parameterized tests when multiple inputs exercise the same rule clearly.

4. **Keep the test isolated.** Exercise the unit without starting a database, network service, broker, Spring application context, or other infrastructure. Use real values for simple value objects. Replace external or nondeterministic collaborators with the lightest suitable test double: a fake or stub for simple deterministic behavior, or a mock where interaction itself is part of the contract. Avoid mocking the class under test, excessive stubbing, and assertions about incidental call order.

5. **Write for readability.** Use given-when-then or Arrange-Act-Assert with clear separation between setup, action, and assertion. Name tests after the scenario and expected outcome, using the repository's naming convention. Keep setup local and small; extract helpers only for genuinely repeated fixture creation that improves clarity. Prefer explicit, meaningful values over opaque literals, and avoid unrelated assertions in one test.

6. **Assert outcomes, not activity for its own sake.** Make expected and actual values easy to distinguish. Use simple, independently known expected values; do not duplicate production logic in the test to compute the expected result. Assert returned values, state, thrown exception types, and meaningful collaborator effects with the most specific assertion available. Verify mock interactions only when they represent required behavior, not merely to mirror the implementation. Assert exception messages only when the message is part of the expected contract.

7. **Make tests deterministic and independent.** Do not depend on test execution order, shared mutable state, real time delays, random external data, or environmental services. Reset or create fixture state per test. If time or randomness is a dependency, control it through an existing seam or deterministic test double.

8. **Validate incrementally.** Run the narrow test class or method first using the repository's build tool. Fix failures in the touched slice, then run the relevant module test suite when practical. Report the command and result; distinguish test failures from build, environment, or discovery problems.

9. **Keep the suite useful over time.** Treat coverage as a diagnostic for missing behavior, not proof of test quality or a reason to test every line mechanically. Baeldung suggests 80% as a rule of thumb; follow the project's coverage target and prioritize meaningful scenarios. Consider TDD when it helps shape behavior, and rely on automated test execution in the project's CI/build pipeline to catch regressions.

## JUnit 5 and Mockito Pattern

Use `MockitoExtension` to initialize mocks without manual reset or shared mock setup. Construct the unit explicitly when practical so its dependencies stay visible. Stub only the collaborator behavior needed for the scenario, then assert the observable result. Add `verify` only when the interaction itself is required behavior.

```java
@ExtendWith(MockitoExtension.class)
class ExampleServiceTest {
	@Mock
	private ExampleRepository repository;

	private ExampleService service;

	@BeforeEach
	void setUp() {
		service = new ExampleService(repository);
	}

	@Test
	void returnsTheRequestedItem() {
		Item expected = new Item("item-1");
		when(repository.findById("item-1")).thenReturn(expected);

		Item actual = service.getItem("item-1");

		assertSame(expected, actual);
	}
}
```

Use the Mockito APIs consistently with the project imports. Add `verify(repository).save(...)` or equivalent only when saving or delegation is part of the behavior being guaranteed, not just because a mock was used.

## Quality Checklist

- Does the test fail for a plausible regression in the behavior it claims to cover?
- Is it testing public, externally observable behavior instead of private structure?
- Is the test isolated from infrastructure and independent of other tests?
- Are cases chosen for normal behavior, meaningful boundaries, and contractually relevant failures?
- Are doubles limited to dependencies that need isolation, with interactions verified only when meaningful?
- Can another developer understand the scenario and expected result without tracing the production implementation?
- Was the focused test command run, and are any verification gaps stated clearly?

## Naming Example

Prefer names that reveal the condition and expected outcome, such as `givenValidRange_whenRequestingPrimes_thenRequestIsSaved`, over vague names such as `testRequest`. Follow existing project naming conventions when they differ.

## Reference

- [Baeldung: Java Unit Testing Best Practices](https://www.baeldung.com/java-unit-testing-best-practices)
