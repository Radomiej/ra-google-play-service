// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "RaGooglePlayService",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "RaGooglePlayService",
            targets: ["PGServicePlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "PGServicePlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/PGServicePlugin"),
        .testTarget(
            name: "PGServicePluginTests",
            dependencies: ["PGServicePlugin"],
            path: "ios/Tests/PGServicePluginTests")
    ]
)