---
name: conventional-commit-message
description: "Create or improve commit messages in English using Conventional Commits 1.0.0. Use when drafting, reviewing, or correcting a Git commit message."
argument-hint: "Describe the changes or ask for a commit message from the current Git diff"
---

# Conventional Commit Message

Create clear English commit messages that follow [Conventional Commits 1.0.0](https://www.conventionalcommits.org/en/v1.0.0/).

## Procedure

1. Inspect the relevant changes before writing a message. For the current worktree, check `git status` and review the staged diff; review the unstaged diff too when the user asks about all current changes. If the user supplies a description or diff, use that evidence. Never claim changes that are not supported by the evidence.
2. Identify the primary change and choose a concise type. Use `feat` for a new feature and `fix` for a bug fix. Other useful types include `build`, `ci`, `docs`, `refactor`, `perf`, `test`, and `chore`; the specification does not mandate these additional types or assign them SemVer effects.
3. Add an optional scope in parentheses when it usefully identifies the affected area. Keep the scope concise and use a noun, such as `api`, `processor`, or `tests`.
4. Write the subject in English, briefly and specifically summarizing the change after the required colon and space. Prefer a concise, action-oriented phrase; do not add a period at the end.
5. Add a body only when context is useful, separating it from the subject with a blank line. Use the body to explain motivation, relevant details, or consequences rather than repeat the subject.
6. Include issue references or other trailers only when supported by the available context. Separate footers from the body with a blank line.
7. For a breaking change, mark it with `!` before the colon, add a `BREAKING CHANGE: <description>` footer, or use both. Describe the incompatible change clearly. Keep `BREAKING CHANGE` uppercase when used as a footer.
8. Return the message in a code block. If essential information is missing to classify or describe the change accurately, ask a focused question rather than inventing details. Do not create a commit unless the user explicitly asks you to commit.

## Format

```text
<type>[optional scope][!]: <description>

[optional body]

[optional footer(s)]
```

The subject is required. The body and footers are optional. A footer uses a token followed by `: ` or ` #`, for example `Fixes: #123`.

## Examples

```text
feat(processor): support configurable batch size
```

```text
fix(api): reject requests with an empty range

Validate the requested range before starting asynchronous processing.
```

```text
feat(api)!: require an authorization token

BREAKING CHANGE: API clients must send an authorization token with every request.
```
